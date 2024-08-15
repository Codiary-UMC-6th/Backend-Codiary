package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.PostConverter;
import com.codiary.backend.global.converter.ProjectConverter;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Project;
import com.codiary.backend.global.service.ProjectService.ProjectQueryService;
import com.codiary.backend.global.web.dto.Post.PostResponseDTO;
import com.codiary.backend.global.web.dto.Project.ProjectResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RequestMapping("/projects")
@Slf4j
@Tag(name = "프로젝트 API", description = "프로젝트 조회 관련 API입니다.")
public class ProjectController {

    private final ProjectQueryService projectQueryService;

    // 프로젝트 설정을 위한 프로젝트 리스트 조회
    @GetMapping("/list")
    @Operation(summary = "프로젝트 리스트 조회 API", description = "프로젝트 설정을 위한 프로젝트 전체 리스트를 조회합니다.", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<ProjectResponseDTO.ProjectPreviewListDTO> findProjects(){
        List<Project> projects = projectQueryService.getProjects();
        return ApiResponse.onSuccess(SuccessStatus.PROJECT_OK, ProjectConverter.toProjectPreviewListDTO(projects));
    }
}
