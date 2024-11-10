package com.codiary.backend.domain.post.dto.response;

import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

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

    @Getter
    @Builder
    @AllArgsConstructor
    //@NoArgsConstructor
    public static class CreatePostResultDTO {
        Long postId;
        Long memberId;
        Long teamId;
        Long projectId;
        String postTitle;
        String postBody;
        String thumbnailImageUrl;
        Boolean postStatus;
        String postCategory;
        Set<Long> coauthorIds;
        PostAccess postAccess;
        PostFileResponseDTO.PostFileListDTO postFileList;
    }

    @Getter
    @Builder
    //@NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePostResultDTO {
        Long postId;
        Long memberId;
        Long teamId;
        Long projectId;
        String postTitle;
        String postBody;
        String thumbnailImageUrl;
        Boolean postStatus;
        String postCategory;
        Set<Long> coauthorIds;
        PostAccess postAccess;
        PostFileResponseDTO.PostFileListDTO postFileList;
    }

}
