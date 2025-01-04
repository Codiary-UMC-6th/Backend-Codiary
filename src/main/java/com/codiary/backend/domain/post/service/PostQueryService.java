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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        List<Post> postsByMember = postRepository.findByMemberOrderByCreatedAtDescPostIdDesc(member, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        List<Post> postsByCoauthor = postRepository.findByAuthorList_MemberOrderByCreatedAtDescPostIdDesc(member, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

        List<Post> combinedPosts = new ArrayList<>();
        combinedPosts.addAll(postsByMember);
        combinedPosts.addAll(postsByCoauthor);

        if (combinedPosts.isEmpty()) { throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_MEMBER); }
        combinedPosts.sort(Comparator.comparing(Post::getCreatedAt).reversed());

        int start = Math.min(page * size, combinedPosts.size());
        int end = Math.min((page + 1) * size, combinedPosts.size());
        return new PageImpl<>(combinedPosts.subList(start, end), request, combinedPosts.size());
    }

//    public Page<Post> getPostsByTitle(Optional<String> optSearch, int page, int size) {
//        PageRequest request = PageRequest.of(page, size);
//        if (optSearch.isPresent()) {
//            String search = optSearch.get();
//            return postRepository.findAllByPostTitleContainingIgnoreCaseOrderByCreatedAtDesc(search, request);
//        }
//        // 검색어 존재 X
//        return postRepository.findAllByOrderByCreatedAtDesc(request);
//    }

    public Page<Post> getPostsByTitle(Optional<String> optSearch, int page, int size) {
        PageRequest request = PageRequest.of(page, size);

        // 모든 게시글을 조회
        Page<Post> allPosts;
        if (optSearch.isPresent()) {
            String search = optSearch.get();
            allPosts = postRepository.findAllByPostTitleContainingIgnoreCaseOrderByCreatedAtDesc(search, request);
        } else {
            allPosts = postRepository.findAllByOrderByCreatedAtDesc(request);
        }

        // 인증된 사용자 가져오기
        Member member = getAuthenticatedMember();

        // validatePostAccess를 활용하여 접근 권한이 있는 게시글만 필터링
        List<Post> accessiblePosts = allPosts.getContent().stream()
                .filter(post -> {
                    try {
                        validatePostAccess(post, member); // 권한 확인
                        return true; // 권한이 있으면 포함
                    } catch (GeneralException e) {
                        return false; // 권한 없으면 제외
                    }
                })
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
        Team team = teamRepository.findById(teamId).get();

        if (!postRepository.existsByTeam(team)){ throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_TEAM); }
        return postRepository.findByTeamOrderByCreatedAtDescPostIdDesc(team, request);
    }


    public Page<Post> getPostsByMemberInProject(Long projectId, Long memberId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.PROJECT_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (!postRepository.existsByProject(project)) { throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_PROJECT); }
        List<Post> postsByMember = postRepository.findByProjectAndMemberOrderByCreatedAtDescPostIdDesc(project, member, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        List<Post> postsByCoauthor = postRepository.findByProjectAndAuthorList_MemberOrderByCreatedAtDescPostIdDesc(project, member, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

        List<Post> combinedPosts = new ArrayList<>();
        combinedPosts.addAll(postsByMember);
        combinedPosts.addAll(postsByCoauthor);

        if (combinedPosts.isEmpty()) { throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_MEMBER); }
        combinedPosts.sort(Comparator.comparing(Post::getCreatedAt).reversed());

        int start = Math.min(page * size, combinedPosts.size());
        int end = Math.min((page + 1) * size, combinedPosts.size());
        return new PageImpl<>(combinedPosts.subList(start, end), request, combinedPosts.size());
    }

    public Page<Post> getPostsByTeamInProject(Long projectId, Long teamId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Project project = projectRepository.findById(projectId).get();
        Team team = teamRepository.findById(teamId).get();

        if (!postRepository.existsByProject(project)){ throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_PROJECT); }
        if (!postRepository.existsByTeam(team)){ throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_TEAM); }
        return postRepository.findByProjectAndTeamOrderByCreatedAtDescPostIdDesc(project, team, request);
    }

    public Page<Post> getPostsByMemberInTeam(Long teamId, Long memberId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (!postRepository.existsByTeam(team)) { throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_TEAM); }
        if (!postRepository.existsByMember(member)) { throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_MEMBER); }

        List<Post> postsByMember = postRepository.findByTeamAndMemberOrderByCreatedAtDescPostIdDesc(team, member, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        List<Post> postsByCoauthor = postRepository.findByTeamAndAuthorList_MemberOrderByCreatedAtDescPostIdDesc(team, member, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

        List<Post> combinedPosts = new ArrayList<>();
        combinedPosts.addAll(postsByMember);
        combinedPosts.addAll(postsByCoauthor);

        if (combinedPosts.isEmpty()) { throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_MEMBER); }
        combinedPosts.sort(Comparator.comparing(Post::getCreatedAt).reversed());

        int start = Math.min(page * size, combinedPosts.size());
        int end = Math.min((page + 1) * size, combinedPosts.size());
        return new PageImpl<>(combinedPosts.subList(start, end), request, combinedPosts.size());
    }

    public Post.PostAdjacent findAdjacentPosts(Long postId) {
        return Post.PostAdjacent.builder()
                .olderPost(postRepository.findTopByPostIdLessThanOrderByCreatedAtDescPostIdDesc(postId).orElse(null))
                .laterPost(postRepository.findTopByPostIdGreaterThanOrderByCreatedAtAscPostIdAsc(postId).orElse(null))
                .build();
    }


    public Page<Post> getPostsByFollowing(Long id, Pageable pageable) {
        //validation
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //return
        return postRepository.findPostsByFollowing(id, pageable);
    }

    public Page<Post> getBookmarkPost(Long memberId, Pageable pageable){
        //validation
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        return postRepository.findByBookmarkPostList(member, pageable);
    }
}
