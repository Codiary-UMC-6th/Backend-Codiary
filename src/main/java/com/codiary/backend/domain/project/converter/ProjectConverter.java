package com.codiary.backend.domain.project.converter;

import com.codiary.backend.domain.member.converter.MemberConverter;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.project.dto.response.ProjectResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectConverter {
    public static ProjectResponseDTO.ProjectDetailResponseDTO toProjectDetailResponseDTO(Project project) {
        Boolean isTeam = project.getTeam() != null;
        return ProjectResponseDTO.ProjectDetailResponseDTO.builder()
                .projectId(project.getProjectId())
                .name(project.getProjectName())
                .isTeam(isTeam)
                .projectMembers(
                        isTeam
                                ? project.getTeam().getTeamMemberList().stream()
                                .map(teamMember -> MemberConverter.toSimpleMemberProfileResponseDto(teamMember.getMember()))
                                .collect(Collectors.toList())
                                : List.of(MemberConverter.toSimpleMemberProfileResponseDto(project.getMember()))
                )
                .build();
    }

    public static ProjectResponseDTO.SimpleProjectResponseDTO toSimpleProjectResponseDTO(Project project) {
        return ProjectResponseDTO.SimpleProjectResponseDTO.builder()
                .projectId(project.getProjectId())
                .name(project.getProjectName())
                .build();
    }

    public static List<ProjectResponseDTO.SimpleProjectResponseDTO> toSimpleProjectListResponseDTO(List<Project> projects) {
        return projects.stream()
                .map(ProjectConverter::toSimpleProjectResponseDTO)
                .collect(Collectors.toList());
    }
}
