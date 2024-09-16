package com.codiary.backend.global.web.dto.Project;

import lombok.Builder;

import java.util.List;

public class ProjectResponseDTO {

    @Builder
    public record ProjectPreviewDTO(Long projectId, String projectName) {}

    @Builder
    public record ProjectPreviewListDTO(List<ProjectPreviewDTO> projects) {}
}
