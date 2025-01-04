package com.codiary.backend.domain.post.service;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.codiary.backend.domain.post.repository.PostRepository;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.project.repository.ProjectRepository;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.repository.TeamRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.apiPayload.exception.handler.PostHandler;

import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostQueryService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;

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


    public Post findById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));
        Member member = getAuthenticatedMember();
        validatePostAccess(post, member);
        return post;
    }


    public Page<Post> getPostsByMember(Long memberId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Member requestedMember = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Member authenticatedMember = getAuthenticatedMember();
        // 게시글 조회
        List<Post> postsByMember = postRepository.findByMemberOrderByCreatedAtDescPostIdDesc(requestedMember, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        List<Post> postsByCoauthor = postRepository.findByAuthorList_MemberOrderByCreatedAtDescPostIdDesc(requestedMember, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        // 게시글 조회 중복 제거
        Set<Post> uniquePosts = new HashSet<>();
        uniquePosts.addAll(postsByMember);
        uniquePosts.addAll(postsByCoauthor);
        // 전체 공개 게시글만 반환할지 여부 결정
        boolean isSameMember = authenticatedMember.equals(requestedMember);
        List<Post> accessiblePosts = uniquePosts.stream()
                .filter(post -> {
                    try {
                        if (isSameMember) { validatePostAccess(post, authenticatedMember);
                        }
                        else { // 다른 사용자: 전체 공개 게시글만 허용
                            if (post.getPostAccess() != PostAccess.ENTIRE) { throw new GeneralException(ErrorStatus.NO_ACCESS_PERMISSION); }
                        } return true;
                    } catch (GeneralException e) { return false; }})
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .toList();
        // 접근 가능한 게시글이 없으면 예외 발생
        if (accessiblePosts.isEmpty()) { throw new GeneralException(ErrorStatus.NO_ACCESS_PERMISSION); }

        int start = Math.min(page * size, accessiblePosts.size());
        int end = Math.min((page + 1) * size, accessiblePosts.size());
        return new PageImpl<>(accessiblePosts.subList(start, end), request, accessiblePosts.size());
    }


    public Page<Post> getPostsByTitle(Optional<String> optSearch, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Page<Post> allPosts;
        if (optSearch.isPresent()) { String search = optSearch.get();
            allPosts = postRepository.findAllByPostTitleContainingIgnoreCaseOrderByCreatedAtDesc(search, request);
        } else { allPosts = postRepository.findAllByOrderByCreatedAtDesc(request); }

        Member member = getAuthenticatedMember();
        List<Post> accessiblePosts = allPosts.getContent().stream()
                .filter(post -> {
                    try {validatePostAccess(post, member);
                        return true; }
                    catch (GeneralException e) { return false; } })
                .toList();
        // 필터링된 결과를 Page로 반환
        return new PageImpl<>(accessiblePosts, request, accessiblePosts.size());
    }


    public Page<Post> getPostsByCategories(Optional<String> optSearch, int page, int size) {
        Pageable request = PageRequest.of(page, size);
        if (optSearch.isPresent()) {
            String search = optSearch.get();
            log.info("Searching posts by category with keyword: {}", search);
            List<Long> postIds = postRepository.findPostIdsByCategoryName(search);
            if (postIds.isEmpty()) { return Page.empty(request); }
            return postRepository.findByPostIdIn(postIds, request);
        }
        return postRepository.findAllByOrderByCreatedAtDesc(request);
    }


    public Page<Post> getPostsByTeam(Long teamId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Team team = teamRepository.findById(teamId)
             .orElseThrow(() -> new PostHandler(ErrorStatus.TEAM_NOT_FOUND));
        if (!postRepository.existsByTeam(team)) { throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_TEAM); }

        Member member = getAuthenticatedMember();
        Page<Post> allPosts = postRepository.findByTeamOrderByCreatedAtDescPostIdDesc(team, request);
        // 모든 게시글에 대해 접근 권한 확인
        boolean hasAccess = allPosts.getContent().stream()
                .allMatch(post -> {
                    try { validatePostAccess(post, member);
                        return true; }
                    catch (GeneralException e) { return false; }});
        // 접근 권한이 없으면 예외 발생
        if (!hasAccess) { throw new GeneralException(ErrorStatus.NO_ACCESS_PERMISSION);}
        return allPosts;
    }


    public Page<Post> getPostsByMemberInProject(Long projectId, Long memberId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new PostHandler(ErrorStatus.PROJECT_NOT_FOUND));
        Member requestedMember = memberRepository.findById(memberId).orElseThrow(() -> new PostHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Member authenticatedMember = getAuthenticatedMember();

        if (!postRepository.existsByProject(project)) {throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_PROJECT);}

        List<Post> postsByMember = postRepository.findByProjectAndMemberOrderByCreatedAtDescPostIdDesc(
                project, requestedMember, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        List<Post> postsByCoauthor = postRepository.findByProjectAndAuthorList_MemberOrderByCreatedAtDescPostIdDesc(
                project, requestedMember, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

        Set<Post> uniquePosts = new HashSet<>();
        uniquePosts.addAll(postsByMember);
        uniquePosts.addAll(postsByCoauthor);

        List<Post> accessiblePosts = uniquePosts.stream()
                .filter(post -> {
                    try {
                        if (requestedMember.equals(authenticatedMember)) { validatePostAccess(post, authenticatedMember); }
                        else { // 다른 사용자: 전체 공개 게시글만 허용
                            if (post.getPostAccess() != PostAccess.ENTIRE) { throw new GeneralException(ErrorStatus.NO_ACCESS_PERMISSION);}
                        }
                        return true;
                    } catch (GeneralException e) { return false; }})
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .toList();

        if (accessiblePosts.isEmpty()) { throw new PostHandler(ErrorStatus.NO_ACCESS_PERMISSION); }

        int start = Math.min(page * size, accessiblePosts.size());
        int end = Math.min((page + 1) * size, accessiblePosts.size());
        return new PageImpl<>(accessiblePosts.subList(start, end), request, accessiblePosts.size());
    }


    public Page<Post> getPostsByTeamInProject(Long projectId, Long teamId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new PostHandler(ErrorStatus.PROJECT_NOT_FOUND));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new PostHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member authenticatedMember = getAuthenticatedMember();

        if (!postRepository.existsByProject(project)) { throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_PROJECT); }
        if (!postRepository.existsByTeam(team)) { throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_TEAM); }

        Page<Post> allPosts = postRepository.findByProjectAndTeamOrderByCreatedAtDescPostIdDesc(project, team, request);

        List<Post> accessiblePosts = allPosts.getContent().stream()
                .filter(post -> {
                    try {
                        validatePostAccess(post, authenticatedMember);
                        return true;
                    } catch (GeneralException e) { return false; }
                })
                .toList();
        if (accessiblePosts.isEmpty()) { throw new PostHandler(ErrorStatus.NO_ACCESS_PERMISSION); }

        return new PageImpl<>(accessiblePosts, request, accessiblePosts.size());
    }


    public Page<Post> getPostsByMemberInTeam(Long teamId, Long memberId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new PostHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member requestedMember = memberRepository.findById(memberId).orElseThrow(() -> new PostHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Member authenticatedMember = getAuthenticatedMember();

        if (!postRepository.existsByTeam(team)) { throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_TEAM); }
        if (!postRepository.existsByMember(requestedMember)) { throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_MEMBER); }

        List<Post> postsByMember = postRepository.findByTeamAndMemberOrderByCreatedAtDescPostIdDesc(
                team, requestedMember, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        List<Post> postsByCoauthor = postRepository.findByTeamAndAuthorList_MemberOrderByCreatedAtDescPostIdDesc(
                team, requestedMember, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

        Set<Post> uniquePosts = new HashSet<>();
        uniquePosts.addAll(postsByMember);
        uniquePosts.addAll(postsByCoauthor);

        List<Post> accessiblePosts = uniquePosts.stream()
                .filter(post -> {
                    try {
                        if (requestedMember.equals(authenticatedMember)) {
                            validatePostAccess(post, authenticatedMember);
                        } else {
                            if (post.getPostAccess() != PostAccess.ENTIRE) { throw new GeneralException(ErrorStatus.NO_ACCESS_PERMISSION); }
                        }
                        return true;
                    } catch (GeneralException e) { return false; }
                })
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed()) // 정렬
                .toList();
        if (accessiblePosts.isEmpty()) { throw new PostHandler(ErrorStatus.NO_ACCESS_PERMISSION); }

        int start = Math.min(page * size, accessiblePosts.size());
        int end = Math.min((page + 1) * size, accessiblePosts.size());
        return new PageImpl<>(accessiblePosts.subList(start, end), request, accessiblePosts.size());
    }


    public Post.PostAdjacent findAdjacentPosts(Long postId) {
        return Post.PostAdjacent.builder()
                .olderPost(postRepository.findTopByPostIdLessThanOrderByCreatedAtDescPostIdDesc(postId).orElse(null))
                .laterPost(postRepository.findTopByPostIdGreaterThanOrderByCreatedAtAscPostIdAsc(postId).orElse(null))
                .build();
    }


    public Page<Post> getPostsByFollowing(Long id, Pageable pageable) {
        Member authenticatedMember = getAuthenticatedMember();
        if (!authenticatedMember.getMemberId().equals(id)) { throw new GeneralException(ErrorStatus.NO_ACCESS_PERMISSION); }

        Page<Post> allPosts = postRepository.findPostsByFollowing(id, pageable);

        List<Post> accessiblePosts = allPosts.getContent().stream()
                .filter(post -> {
                    try {
                        validatePostAccess(post, authenticatedMember);
                        return true;
                    } catch (GeneralException e) { return false; }
                })
                .toList();
        if (accessiblePosts.isEmpty()) { throw new GeneralException(ErrorStatus.NO_ACCESS_PERMISSION); }

        return new PageImpl<>(accessiblePosts, pageable, accessiblePosts.size());
    }


    public Page<Post> getBookmarkPost(Long memberId, Pageable pageable){
        //validation
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        return postRepository.findByBookmarkPostList(member, pageable);
    }
}
