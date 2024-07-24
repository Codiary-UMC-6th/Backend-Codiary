package com.codiary.backend.global.web.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPreviewDTO {
        Long postId;
        String postTitle;
        Boolean postStatus;
        String postCategory;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPreviewListDTO {
        List<PostPreviewDTO> posts;
    }
}
