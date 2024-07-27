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
    public static class PostPreviewDTO { // Post 조회
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
    public static class PostPreviewListDTO { // 전체 Post 리스트 조회
        List<PostPreviewDTO> posts;
        //Integer listSize;
        //Integer totalPage;
        //Long totalElements;
        //boolean isFirst;
        //boolean isLast;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberPostResultDTO { // 저자별 Post 조회
        Long memberId;
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
    public static class MemberPostResultListDTO { // 저자별 Post 리스트 조회
        List<MemberPostResultDTO> posts;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamPostPreviewDTO { // 팀별 Post 조회
        Long teamId;
        Long postId;
        Long memberId;
        String postTitle;
        Boolean postStatus;
        String postCategory;
        Set<Long> coauthorIds;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamPostPreviewListDTO { // 팀별 Post 리스트 조회
        List<TeamPostPreviewDTO> posts;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        boolean isFirst;
        boolean isLast;
    }

}
