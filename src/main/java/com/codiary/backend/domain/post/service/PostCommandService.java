package com.codiary.backend.domain.post.service;

import com.codiary.backend.domain.category.entity.Category;
import com.codiary.backend.domain.category.service.CategoryService;
import com.codiary.backend.domain.coauthor.entity.Author;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.member.service.MemberCommandService;
import com.codiary.backend.domain.post.converter.PostConverter;
import com.codiary.backend.domain.post.converter.PostFileConverter;
import com.codiary.backend.domain.post.dto.request.PostRequestDTO;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.entity.PostFile;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.codiary.backend.domain.post.repository.AuthorRepository;
import com.codiary.backend.domain.post.repository.PostFileRepository;
import com.codiary.backend.domain.post.repository.PostRepository;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.project.repository.ProjectRepository;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.entity.TeamMember;
import com.codiary.backend.domain.team.repository.TeamRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.codiary.backend.global.apiPayload.exception.handler.PostHandler;
import com.codiary.backend.global.common.uuid.Uuid;
import com.codiary.backend.global.common.uuid.UuidRepository;
import com.codiary.backend.global.s3.AmazonS3Manager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostCommandService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final AuthorRepository authorRepository;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;
    private final UuidRepository uuidRepository;
    private final PostFileRepository postFileRepository;
    private final MemberCommandService memberCommandService;
    private final CategoryService categoryService;
    private final AmazonS3Manager s3Manager;


    private void validatePostAccess(Post post, Member member) {
        // MEMBER 접근 권한: 작성자만 접근 가능
        if (post.getPostAccess() == PostAccess.MEMBER && !post.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.NO_ACCESS_PERMISSION);
        }
        // TEAM 접근 권한: 팀 멤버만 접근 가능
        if (post.getPostAccess() == PostAccess.TEAM && post.getTeam() != null) {
            if (!teamRepository.isTeamMember(post.getTeam(), member)) {
                throw new GeneralException(ErrorStatus.NO_ACCESS_PERMISSION);
            }
        }
    }

    private Member getAuthenticatedMember() {
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // username (식별자) 가져오기
        String username = authentication.getName();
        // username으로 Member 엔터티 조회
        return memberRepository.findByEmail(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }


    // 포스트 생성
    public Post createPost(Long memberId, PostRequestDTO.CreatePostRequestDTO request) {
        // validation: member|team|project 유무 확인 (team 및 project 없는 경우 null)
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        // 팀 설정 및 멤버 검증
        Team team = null;
        if (request.getTeamId() != null) {
            team = teamRepository.findById(request.getTeamId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

            // 팀 멤버 검증 (팀 설정은 자신이 속한 팀만 설정 가능)
            if (!teamRepository.isTeamMember(team, member)) {
                throw new GeneralException(ErrorStatus.TEAM_MEMBER_ONLY_ACCESS);
            }
        }

        // PostAccess가 TEAM인데 팀이 null인 경우 예외 처리
        if (request.getPostAccess() == PostAccess.TEAM && team == null) {
            throw new GeneralException(ErrorStatus.TEAM_REQUIRED_FOR_ACCESS);
        }

        Project project = request.getProjectId() == null ? null
                : projectRepository.findById(request.getProjectId()).orElse(null);

        Post newPost = PostConverter.toPost(request, team, project, member);
        Post tempPost = postRepository.save(newPost);

        // 팀 post의 경우 팀 멤버를 공통 저자로 추가
        if (team != null) {
            if (!teamRepository.isTeamMember(team, member)) {
                throw new GeneralException(ErrorStatus.TEAM_MEMBER_ONLY_ACCESS);
            }
            List<Author> authorList = new ArrayList<>();
            for (TeamMember teamMember : team.getTeamMemberList()) {
                Author author = Author.builder()
                        .member(teamMember.getMember())
                        .post(tempPost)
                        .build();
                authorList.add(author);
                authorRepository.save(author);
            }
            tempPost.setAuthorList(authorList);
        }

        // 파일 업로드 처리
        if (request.getPostFiles() != null) {
            for (MultipartFile file : request.getPostFiles()) {
                if (file.isEmpty()) {
                    continue;
                }
                String uuid = UUID.randomUUID().toString();
                Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
                String fileUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedUuid), file);

                PostFile newPostFile = PostFileConverter.toPostFile(fileUrl, newPost, file.getOriginalFilename());
                postFileRepository.save(newPostFile);

                tempPost.getPostFileList().add(newPostFile);
            }
        }

        // 대표 사진 설정
        String thumbnailImageName = request.getThumbnailImageName();
        for (PostFile postFile : tempPost.getPostFileList()) {
            if (postFile.getFileName().equals(thumbnailImageName)) {
                tempPost.setThumbnailImage(postFile);
            }
        }
        if (tempPost.getPostFileList().size() != 0 && tempPost.getThumbnailImage() == null) {
            tempPost.setThumbnailImage(tempPost.getPostFileList().get(0));
        }

        Post savedPost = postRepository.save(tempPost);
        return savedPost;
    }


    public Post updatePost(Long postId, Long memberId, PostRequestDTO.UpdatePostDTO request) {
        // validation: 다이어리 및 멤버 유무 확인
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // validation: 수정 권한 확인 (작성자 or 공동 작성자 유무)
        if (!(post.getMember().equals(member) || authorRepository.existsByPostAndMember(post, member))) {
            throw new PostHandler(ErrorStatus.POST_UPDATE_UNAUTHORIZED);
        }

        post.update(request);

        // 새로운 이미지 추가
        if (request.getAddedPostFiles() != null) {
            for (MultipartFile file : request.getAddedPostFiles()) {
                if (file.isEmpty()) {
                    continue;
                }
                String uuid = UUID.randomUUID().toString();
                Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
                String fileUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedUuid), file);

                PostFile newPostFile = PostFileConverter.toPostFile(fileUrl, post, file.getOriginalFilename());
                postFileRepository.save(newPostFile);

                post.getPostFileList().add(newPostFile);
            }
        }

        // 대표 사진 설정
        String thumbnailImageName = request.getThumbnailImageName();
        for (PostFile postFile : post.getPostFileList()) {
            if (postFile.getFileName() == thumbnailImageName) {
                post.setThumbnailImage(postFile);
            }
        }
        if (post.getPostFileList().size() != 0 && post.getThumbnailImage() == null) {
            post.setThumbnailImage(post.getPostFileList().get(0));
        }

        return postRepository.save(post);
    }


    public void deletePost(Long postId) {
        Member getMember = memberCommandService.getRequester();

        Post deletePost = postRepository.findById(postId).get();
        postRepository.delete(deletePost);
    }


    public Post setPostCategories(Long postId, Set<String> categoryNames) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 카테고리 이름으로 Categories 엔티티를 생성하거나 조회
        List<Category> categories = categoryNames.stream()
                .map(name -> {
                    // 카테고리 이름으로 Categories 엔티티를 조회하거나 새로 생성
                    return categoryService.addCategory(post, name);
                })
                .collect(Collectors.toList());

        // 포스트에 카테고리를 설정
        post.setCategories(categories);

        return postRepository.save(post);
    }


    public Post updateCoauthors(Long postId, PostRequestDTO.UpdateCoauthorRequestDTO request) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));
        Member authenticatedMember = getAuthenticatedMember();
        validatePostAccess(post, authenticatedMember);
        // 기존 공동 저자 리스트 삭제
        post.getAuthorList().clear();
        // 새로운 공동 저자 리스트 추가
        Set<Author> coauthors = request.getMemberIds().stream()
                .map(newCoauthorId -> {
                    Member newCoauthor = memberRepository.findById(newCoauthorId).orElseThrow(() -> new IllegalArgumentException("Member not found: " + newCoauthorId));
                    return Author.createAuthors(post, newCoauthor);
                })
                .collect(Collectors.toSet());
        post.getAuthorList().addAll(coauthors);

        return postRepository.save(post);
    }


    public Post setPostTeam(Long postId, Long teamId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Member authenticatedMember = getAuthenticatedMember();
        validatePostAccess(post, authenticatedMember);

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));
        post.setTeam(team);

        return postRepository.save(post);
    }




}
