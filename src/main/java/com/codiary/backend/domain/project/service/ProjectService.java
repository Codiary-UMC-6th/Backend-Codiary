package com.codiary.backend.domain.project.service;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.project.repository.ProjectRepository;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.repository.TeamRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public Project createPersonalProject(Long memberId, String projectName) {
        //validation
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Project existingProject = projectRepository.findByProjectNameAndDeletedAtIsNull(projectName)
                .orElse(null);

        // business
        if (existingProject != null) { // 프로젝트 이름 중복 확인
            throw new GeneralException(ErrorStatus.PROJECT_ALREADY_EXISTS);
        } else {
            Project project = Project.builder()
                    .projectName(projectName)
                    .team(null)
                    .member(member)
                    .build();
            projectRepository.save(project);
            return project;
        }
    }

    public List<Project> getMemberProject(Long memberId, Long currentId) {
        //validation
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Member currentMember = memberRepository.findById(currentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //return
        return projectRepository.findByMemberProjectMapsMember(member);
    }

    @Transactional
    public Project createTeamProject(Long id, Long teamId, String projectName) {
        // validation
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Team team = teamRepository.findByTeamIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        if (teamRepository.isTeamMember(team, member)) {
            throw new GeneralException(ErrorStatus.TEAM_MEMBER_ONLY_ACCESS);
        }

        Project existingProject = projectRepository.findByProjectNameAndDeletedAtIsNull(projectName)
                .orElse(null);

        // business
        if (existingProject != null) { // 프로젝트 이름 중복 확인
            throw new GeneralException(ErrorStatus.PROJECT_ALREADY_EXISTS);
        } else {
            Project project = Project.builder()
                    .projectName(projectName)
                    .team(team)
                    .member(member)
                    .build();
            projectRepository.save(project);
            return project;
        }
    }

    public List<Project> getTeamProject(Long teamId, Long memberId) {
        // validation
        Team team = teamRepository.findByTeamIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (teamRepository.isTeamMember(team, member)) {
            throw new GeneralException(ErrorStatus.TEAM_MEMBER_ONLY_ACCESS);
        }

        // return
        return projectRepository.findByTeamProjectMapsTeamId(teamId);
    }
}
