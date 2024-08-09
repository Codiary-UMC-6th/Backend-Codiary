package com.codiary.backend.global.web.dto.Post;

import com.codiary.backend.global.domain.enums.PostAccess;
import com.codiary.backend.global.web.dto.PostFile.PostFileRequestDTO;
import com.codiary.backend.global.web.dto.PostFile.PostFileResponseDTO;
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
    @NoArgsConstructor
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPreviewDTO { // Post 조회
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
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPreviewListDTO { // 전체 Post 리스트 조회
        List<PostPreviewDTO> posts;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        boolean isFirst;
        boolean isLast;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberPostPreviewDTO { // 저자별 Post 조회
        Long memberId;
        Long postId;
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
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberPostPreviewListDTO { // 저자별 Post 리스트 조회
        List<MemberPostPreviewDTO> posts;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        boolean isFirst;
        boolean isLast;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamPostPreviewDTO { // 팀별 Post 조회
        Long teamId;
        Long postId;
        Long memberId;
        Long projectId;
        String postTitle;
        String postBody;
        String thumbnailImageUrl;
        Boolean postStatus;
        String postCategory;
        Set<Long> coauthorIds;
        PostAccess postAccess;
        PostFileResponseDTO.PostFileListDTO postFileList;
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberPostInProjectPreviewDTO { // 프로젝트별 저자의 Post 조회
        Long projectId;
        Long memberId;
        Long postId;
        Long teamId;
        String postTitle;
        String postBody;
        String thumbnailImageUrl;
        Boolean postStatus;
        String postCategory;
        Set<Long> coauthorIds;
        PostAccess postAccess;
        PostFileResponseDTO.PostFileListDTO postFileList;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberPostInProjectPreviewListDTO { // 프로젝트별 저자의 Post 리스트 조회
        List<MemberPostInProjectPreviewDTO> posts;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        boolean isFirst;
        boolean isLast;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamPostInProjectPreviewDTO { // 프로젝트별 팀의 Post 조회
        Long projectId;
        Long teamId;
        Long postId;
        Long memberId;
        String postTitle;
        String postBody;
        String thumbnailImageUrl;
        Boolean postStatus;
        String postCategory;
        Set<Long> coauthorIds;
        PostAccess postAccess;
        PostFileResponseDTO.PostFileListDTO postFileList;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamPostInProjectPreviewListDTO { // 프로젝트별 팀의 Post 리스트 조회
        List<TeamPostInProjectPreviewDTO> posts;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        boolean isFirst;
        boolean isLast;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberPostInTeamPreviewDTO { // 팀별 멤버의 Post 조회
        Long teamId;
        Long memberId;
        Long postId;
        Long projectId;
        String postTitle;
        String postBody;
        String thumbnailImageUrl;
        Boolean postStatus;
        String postCategory;
        Set<Long> coauthorIds;
        PostAccess postAccess;
        PostFileResponseDTO.PostFileListDTO postFileList;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberPostInTeamPreviewListDTO { // 팀별 멤버의 Post 리스트 조회
        List<MemberPostInTeamPreviewDTO> posts;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        boolean isFirst;
        boolean isLast;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostAdjacentDTO {
        Boolean hasLater;
        Boolean hadOlder;
        PostAdjacentPreviewDTO laterPost;
        PostAdjacentPreviewDTO olderPost;

        @Builder
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PostAdjacentPreviewDTO {
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
            LocalDateTime createdAt;
            LocalDateTime updatedAt;
        }
    }

}

