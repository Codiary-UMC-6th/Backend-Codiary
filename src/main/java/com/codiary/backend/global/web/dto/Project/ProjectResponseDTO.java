package com.codiary.backend.global.web.dto.Project;

import com.codiary.backend.global.web.dto.Category.CategoryResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ProjectResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectPreviewDTO {
        Long projectId;
        String projectName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectPreviewListDTO {
        List<ProjectResponseDTO.ProjectPreviewDTO> projects;
    }
}
