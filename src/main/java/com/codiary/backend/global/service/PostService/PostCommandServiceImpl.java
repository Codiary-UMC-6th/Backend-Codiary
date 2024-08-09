package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.converter.PostConverter;
import com.codiary.backend.global.converter.PostFileConverter;
import com.codiary.backend.global.domain.entity.*;
import com.codiary.backend.global.domain.entity.mapping.Authors;
import com.codiary.backend.global.repository.*;
import com.codiary.backend.global.s3.AmazonS3Manager;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Project;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.domain.entity.mapping.Authors;
import com.codiary.backend.global.domain.entity.mapping.Categories;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.repository.PostRepository;
import com.codiary.backend.global.repository.ProjectRepository;
import com.codiary.backend.global.repository.TeamRepository;
import com.codiary.backend.global.service.CategoryService.CategoryCommandService;
import com.codiary.backend.global.service.MemberService.MemberCommandService;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;
    private final CategoryCommandService categoryCommandService;
    private final MemberCommandService memberCommandService;
    private final AmazonS3Manager s3Manager; // 추가
    private final UuidRepository uuidRepository; // 추가
    private final PostFileRepository postFileRepository; // 추가

    @Override
    public Post createPost(PostRequestDTO.CreatePostRequestDTO request) {

        Post newPost = PostConverter.toPost(request);
        Member getMember = memberCommandService.getRequester();

        newPost.setMember(getMember);

        Post tempPost = postRepository.save(newPost);
        tempPost.setPostFileList(new ArrayList<>());

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

        Post savedPost = postRepository.save(tempPost);
        return savedPost;
    }

    @Override
    public Post updatePost(Long postId, PostRequestDTO.UpdatePostDTO request) {
        Member getMember = memberCommandService.getRequester();
        Post updatePost = postRepository.findById(postId).get();
        updatePost.update(request);

        // 새로운 이미지 추가
        if (request.getAddedPostFiles() != null) {
            for (MultipartFile file : request.getAddedPostFiles()) {
                if (file.isEmpty()) {
                    continue;
                }
                String uuid = UUID.randomUUID().toString();
                Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
                String fileUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedUuid), file);

                PostFile newPostFile = PostFileConverter.toPostFile(fileUrl, updatePost, file.getOriginalFilename());
                postFileRepository.save(newPostFile);

                updatePost.getPostFileList().add(newPostFile);
            }
        }

        return postRepository.save(updatePost);
    }

    @Override
    public void deletePost(Long postId) {
        Member getMember = memberCommandService.getRequester();

        Post deletePost = postRepository.findById(postId).get();
        postRepository.delete(deletePost);
    }


    @Override
    public Post updateVisibility(Long postId, PostRequestDTO.UpdateVisibilityRequestDTO request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        Member getMember = memberCommandService.getRequester();

        post.setPostStatus(request.getPostStatus());
        return postRepository.save(post);
    }

    @Override
    public Post updateCoauthors(Long postId, PostRequestDTO.UpdateCoauthorRequestDTO request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        Member getMember = memberCommandService.getRequester();

        // 기존 공동 저자 리스트 삭제
        post.getAuthorsList().clear();

        // 새로운 공동 저자 리스트 추가
        Set<Authors> coauthors = request.getMemberIds().stream()
                .map(newCoauthorId -> {
                    Member newCoauthor = memberRepository.findById(newCoauthorId)
                            .orElseThrow(() -> new IllegalArgumentException("Member not found: " + newCoauthorId));
                    return Authors.createAuthors(post, newCoauthor);
                }).collect(Collectors.toSet());

        post.getAuthorsList().addAll(coauthors);

        return postRepository.save(post);
    }

    @Override
    public Post setPostTeam(Long postId, Long teamId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        Member getMember = memberCommandService.getRequester();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        post.setTeam(team);

        return postRepository.save(post);
    }


    @Override
    public Post setPostCategories(Long postId, Set<String> categoryNames) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 카테고리 이름으로 Categories 엔티티를 생성하거나 조회
        List<Categories> categories = categoryNames.stream()
                .map(name -> {
                    // 카테고리 이름으로 Categories 엔티티를 조회하거나 새로 생성
                    return categoryCommandService.addCategory(post, name);
                })
                .collect(Collectors.toList());

        // 포스트에 카테고리를 설정
        post.setCategories(categories);

        return postRepository.save(post);
    }
}
