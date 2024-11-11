package com.codiary.backend.domain.post.service;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.repository.PostRepository;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.project.repository.ProjectRepository;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.repository.TeamRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.handler.PostHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostQueryService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;

    public Page<Post> getPostsByMember(Long memberId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        List<Post> postsByMember = postRepository.findByMemberOrderByCreatedAtDescPostIdDesc(member, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        List<Post> postsByCoauthor = postRepository.findByAuthorsList_MemberOrderByCreatedAtDescPostIdDesc(member, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

        List<Post> combinedPosts = new ArrayList<>();
        combinedPosts.addAll(postsByMember);
        combinedPosts.addAll(postsByCoauthor);

        if (combinedPosts.isEmpty()) { throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_MEMBER); }
        combinedPosts.sort(Comparator.comparing(Post::getCreatedAt).reversed());

        int start = Math.min(page * size, combinedPosts.size());
        int end = Math.min((page + 1) * size, combinedPosts.size());
        return new PageImpl<>(combinedPosts.subList(start, end), request, combinedPosts.size());
    }

    public Page<Post> getPostsByTitle(Optional<String> optSearch, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        if (optSearch.isPresent()) {
            String search = optSearch.get();
            return postRepository.findAllByPostTitleContainingIgnoreCaseOrderByCreatedAtDesc(search, request);
        }
        // 검색어 존재 X
        return postRepository.findAllByOrderByCreatedAtDesc(request);
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
        List<Post> postsByCoauthor = postRepository.findByProjectAndAuthorsList_MemberOrderByCreatedAtDescPostIdDesc(project, member, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

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
        List<Post> postsByCoauthor = postRepository.findByTeamAndAuthorsList_MemberOrderByCreatedAtDescPostIdDesc(team, member, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

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





}
