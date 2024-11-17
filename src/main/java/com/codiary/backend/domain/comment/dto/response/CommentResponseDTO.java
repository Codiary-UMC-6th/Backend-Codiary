package com.codiary.backend.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import lombok.Builder;

public class CommentResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record CommentDTO(
            Long commentId,
            String commentBody,
            Long postId,
            Long parentId,
            Long commenterId,
            String commenterProfileImageUrl,
            String commenterNickname,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Integer numberOfReply
    ) {
    }
}
