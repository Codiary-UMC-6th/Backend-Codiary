package com.codiary.backend.domain.project.controller;

import com.codiary.backend.domain.member.security.CustomMemberDetails;
import com.codiary.backend.domain.project.converter.ProjectConverter;
import com.codiary.backend.domain.project.dto.response.ProjectResponseDTO;
import com.codiary.backend.domain.project.service.ProjectService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.codiary.backend.domain.project.entity.Project;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/project")
@Tag(name = "프로젝트 API", description = "프로젝트 조회 관련 API입니다.")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "개인 프로젝트 생성", description = "개인 프로젝트를 생성합니다.")
    @PostMapping("/create/{project_name}")
    public ApiResponse<ProjectResponseDTO.SimpleProjectResponseDTO> createPersonalProject(@AuthenticationPrincipal CustomMemberDetails memberDetails,
                                                                 @PathVariable("project_name") String projectName) {
        Project project = projectService.createPersonalProject(memberDetails.getId(), projectName);
        return ApiResponse.onSuccess(SuccessStatus.PROJECT_OK, ProjectConverter.toSimpleProjectResponseDTO(project));
    }

    @Operation(summary = "사용자의 프로젝트 조회", description = "프로젝트를 조회합니다.")
    @GetMapping("/{member_id}")
    public ApiResponse<List<ProjectResponseDTO.SimpleProjectResponseDTO>> getMemberProject(@PathVariable("member_id") Long memberId, @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        List<Project> projects = projectService.getMemberProject(memberId, memberDetails.getId());
        return ApiResponse.onSuccess(SuccessStatus.PROJECT_OK, ProjectConverter.toSimpleProjectListResponseDTO(projects));
    }

    @Operation(summary = "팀 프로젝트 생성", description = "팀 프로젝트를 생성합니다.")
    @PostMapping("/create/team/{team_id}/{project_name}")
    public ApiResponse<ProjectResponseDTO.SimpleProjectResponseDTO> createTeamProject(@AuthenticationPrincipal CustomMemberDetails memberDetails,
                                                                 @PathVariable("team_id") Long teamId,
                                                                 @PathVariable("project_name") String projectName) {
        Project project = projectService.createTeamProject(memberDetails.getId(), teamId, projectName);
        return ApiResponse.onSuccess(SuccessStatus.PROJECT_OK, ProjectConverter.toSimpleProjectResponseDTO(project));
    }

    @Operation(summary = "팀 프로젝트 조회", description = "팀 프로젝트를 조회합니다.")
    @GetMapping("/team/{team_id}")
    public ApiResponse<List<ProjectResponseDTO.SimpleProjectResponseDTO>> getTeamProject(@AuthenticationPrincipal CustomMemberDetails memberDetails,
                                                                                         @PathVariable("team_id") Long teamId) {
        List<Project> projects = projectService.getTeamProject(teamId, memberDetails.getId());
        return ApiResponse.onSuccess(SuccessStatus.PROJECT_OK, ProjectConverter.toSimpleProjectListResponseDTO(projects));
    }
}
