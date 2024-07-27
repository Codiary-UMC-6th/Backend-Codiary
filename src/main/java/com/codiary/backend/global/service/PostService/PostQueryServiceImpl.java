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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public List<Post> findAllBySearch(Optional<String> optSearch) {
        // 만약 검색어가 존재한다면
        if (optSearch.isPresent()) {
            String search = optSearch.get();

            return postRepository.findAllByPostTitleContainingIgnoreCaseOrderByCreatedAtDesc(search);
        }
        // 검색어 존재 X
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Page<Post> getPostsByMember(Long memberId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Member member = memberRepository.findById(memberId).get();

        if (!postRepository.existsByMember(member)){
            throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_MEMBER);
        }
        return postRepository.findByMemberOrderByCreatedAtDescPostIdDesc(member, request);
    }

    @Override
    public Page<Post> getPostsByTeam(Long teamId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Team team = teamRepository.findById(teamId).get();

        if (!postRepository.existsByTeam(team)){
            throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_TEAM);
        }
        return postRepository.findByTeamOrderByCreatedAtDescPostIdDesc(team, request);
    }

    @Override
    public Page<Post> getPostsByMemberInProject(Long projectId, Long memberId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Project project = projectRepository.findById(projectId).get();
        Member member = memberRepository.findById(memberId).get();

        if (!postRepository.existsByProject(project)){
            throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_PROJECT);
        }
        if (!postRepository.existsByMember(member)){
            throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_MEMBER);
        }
        return postRepository.findByProjectAndMemberOrderByCreatedAtDescPostIdDesc(project, member, request);
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
    public Page<Post> getPostsByMemberInTeam(Long teamId, Long memberId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Team team = teamRepository.findById(teamId).get();
        Member member = memberRepository.findById(memberId).get();

        if (!postRepository.existsByTeam(team)){
            throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_TEAM);
        }
        if (!postRepository.existsByMember(member)){
            throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_MEMBER);
        }
        return postRepository.findByTeamAndMemberOrderByCreatedAtDescPostIdDesc(team, member, request);
    }


    @Override
    public Map<String, List<String>> getPostsByMonth(Long memberId, YearMonth yearMonth) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        List<Post> posts = postRepository.findByMemberAndCreatedAtBetweenOrderByCreatedAtAsc(member, startDate, endDate);

        Map<String, List<String>> projectActivities = new HashMap<>();

        posts.forEach(post -> {
            String date = post.getCreatedAt().toLocalDate().toString();
            String projectName = post.getProject().getProjectName();
            projectActivities.computeIfAbsent(date, k -> new ArrayList<>()).add(projectName);
        });

        return projectActivities;
    }
}
