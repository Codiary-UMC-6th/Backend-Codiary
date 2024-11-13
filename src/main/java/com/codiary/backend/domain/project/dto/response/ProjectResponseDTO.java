package com.codiary.backend.domain.project.dto.response;

import lombok.Builder;

public class ProjectResponseDTO {

    @Builder
    public record SimpleProjectResponseDTO(
            Long projectId,
            String name
    ) {
    }
}
