package com.codiary.backend.global.web.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class PostResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreatePostResultDTO {
        Long postId;
        String postTitle;
        //String postBody;
        Boolean postStatus;
        String postCategory;
        Set<Long> coauthorIds;
        Long teamId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePostResultDTO {
        Long postId;
        String postTitle;
        //String postBody;
        Boolean postStatus;
        String postCategory;
        Set<Long> coauthorIds;
        Long teamId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPreviewDTO {
        Long postId;
        Long memberId;
        String postTitle;
        Boolean postStatus;
        String postCategory;
        Set<Long> coauthorIds;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        Long teamId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPreviewListDTO {
        List<PostPreviewDTO> posts;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberPostResultDTO {
        Long postId;
        String postTitle;
        Boolean postStatus;
        String postCategory;
        Set<Long> coauthorIds;
        Long teamId;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberPostResultListDTO {
        List<MemberPostResultDTO> posts;
    }

}
