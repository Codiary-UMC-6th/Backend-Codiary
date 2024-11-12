package com.codiary.backend.domain.post.dto.response;

import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class PostResponseDTO {

    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record SimplePostResponseDTO(
            Long id,
            String title,
            String body,
            String author,
            String authorImage,
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


    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MemberPostPreviewDTO(    // 저자별 Post 조회
            Long memberId,
            Long postId,
            Long teamId,
            Long projectId,
            String postTitle,
            String postBody,
            String thumbnailImageUrl,
            Boolean postStatus,
            String postCategory,
            Set<Long> coauthorIds,
            PostAccess postAccess,
            PostFileResponseDTO.PostFileListDTO postFileList,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
    }

    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MemberPostPreviewListDTO(  // 저자별 Post 리스트 조회
            List<MemberPostPreviewDTO> posts,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            boolean isFirst,
            boolean isLast
    ) {
    }

    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record TeamPostPreviewDTO(
            Long teamId,
            Long postId,
            Long memberId,
            Long projectId,
            String postTitle,
            String postBody,
            String thumbnailImageUrl,
            Boolean postStatus,
            String postCategory,
            Set<Long> coauthorIds,
            PostAccess postAccess,
            PostFileResponseDTO.PostFileListDTO postFileList,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
    }

    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record TeamPostPreviewListDTO ( // 팀별 Post 리스트 조회
        List<TeamPostPreviewDTO> posts,
        Integer listSize,
        Integer totalPage,
        Long totalElements,
        boolean isFirst,
        boolean isLast
    ) {
    }





}
