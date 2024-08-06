package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.apiPayload.exception.handler.PostHandler;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Project;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.repository.PostRepository;
import com.codiary.backend.global.repository.ProjectRepository;
import com.codiary.backend.global.repository.TeamRepository;
import com.codiary.backend.global.service.MemberService.MemberCommandService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@Getter
public class PostQueryServiceImpl implements PostQueryService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final PostRepository postRepository;
    private final ProjectRepository projectRepository;
    private final MemberCommandService memberCommandService;

    @Override
    public Page<Post> getPostsByTitle(Optional<String> optSearch, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        // 만약 검색어가 존재한다면
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
            if (postIds.isEmpty()) {
                return Page.empty(request);
            }
            return postRepository.findByPostIdIn(postIds, request);
        }

        // 검색어가 없는 경우 전체 게시글을 날짜 기준으로 페이지네이션
        return postRepository.findAllByOrderByCreatedAtDesc(request);
    }

    @Override
    public Page<Post> getPostsByMember(int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Member getMember = memberCommandService.getRequester();

        if (!postRepository.existsByMember(getMember)){
            throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_MEMBER);
        }
        return postRepository.findByMemberOrderByCreatedAtDescPostIdDesc(getMember, request);
    }

    @Override
    public Page<Post> getPostsByTeam(Long teamId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Team team = teamRepository.findById(teamId).get();
        Member getMember = memberCommandService.getRequester();

        if (!postRepository.existsByTeam(team)){
            throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_TEAM);
        }
        return postRepository.findByTeamOrderByCreatedAtDescPostIdDesc(team, request);
    }

    @Override
    public Page<Post> getPostsByMemberInProject(Long projectId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Project project = projectRepository.findById(projectId).get();
        Member getMember = memberCommandService.getRequester();

        if (!postRepository.existsByProject(project)){
            throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_PROJECT);
        }
        if (!postRepository.existsByMember(getMember)){
            throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_MEMBER);
        }
        return postRepository.findByProjectAndMemberOrderByCreatedAtDescPostIdDesc(project, getMember, request);
    }

    @Override
    public Page<Post> getPostsByTeamInProject(Long projectId, Long teamId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Project project = projectRepository.findById(projectId).get();
        Team team = teamRepository.findById(teamId).get();

        if (!postRepository.existsByProject(project)){
            throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_PROJECT);
        }
        if (!postRepository.existsByTeam(team)){
            throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_TEAM);
        }
        return postRepository.findByProjectAndTeamOrderByCreatedAtDescPostIdDesc(project, team, request);
    }

    @Override
    public Page<Post> getPostsByMemberInTeam(Long teamId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Team team = teamRepository.findById(teamId).get();
        Member getMember = memberCommandService.getRequester();

        if (!postRepository.existsByTeam(team)){
            throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_TEAM);
        }
        if (!postRepository.existsByMember(getMember)){
            throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_MEMBER);
        }
        return postRepository.findByTeamAndMemberOrderByCreatedAtDescPostIdDesc(team, getMember, request);
    }

    @Override
    public Post.PostAdjacent findAdjacentPosts(Long postId) {
        return Post.PostAdjacent.builder()
                .olderPost(postRepository.findTopByPostIdLessThanOrderByCreatedAtDescPostIdDesc(postId).orElse(null))
                .laterPost(postRepository.findTopByPostIdGreaterThanOrderByCreatedAtAscPostIdAsc(postId).orElse(null))
                .build();
    }


    @Override
    public List<Post> getPostsByMonth(Long memberId, YearMonth yearMonth) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        List<Post> posts = postRepository.findByMemberAndCreatedAtBetweenOrderByCreatedAtAsc(member, startDate, endDate);
        return posts;
    }
}
