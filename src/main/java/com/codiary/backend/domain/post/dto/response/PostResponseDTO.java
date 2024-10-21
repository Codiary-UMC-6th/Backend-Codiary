package com.codiary.backend.domain.post.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.time.LocalDateTime;

public class PostResponseDTO {

    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record SimplePostResponseDTO(
            Long id,
            String title,
            String body,
            String author,
            LocalDateTime createdAt,
            String thumbnailImage
    ) {
    }
}
