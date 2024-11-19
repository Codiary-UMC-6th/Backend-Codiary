package com.codiary.backend.domain.project.service;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.entity.MemberProjectMap;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.project.repository.ProjectRepository;
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

    @Transactional
    public Project createPersonalProject(Long memberId, String projectName) {
        //validation
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Project project = projectRepository.findByProjectNameAndDeletedAtIsNull(projectName)
                .orElse(null);

        //business
        if (project != null) { // 프로젝트 이름 중복 확인
            throw new GeneralException(ErrorStatus.PROJECT_ALREADY_EXISTS);
        } else {
            project = Project.builder()
                    .projectName(projectName)
                    .build();
            projectRepository.save(project);

            MemberProjectMap memberProjectMap = MemberProjectMap.builder()
                    .member(member)
                    .project(project)
                    .build();

            member.addProject(memberProjectMap);
        }

        //return
        return project;
    }

    public List<Project> getMyProject(Long id) {
        //validation
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //return
        return projectRepository.findByMemberProjectMapsMember(member);
    }
}
