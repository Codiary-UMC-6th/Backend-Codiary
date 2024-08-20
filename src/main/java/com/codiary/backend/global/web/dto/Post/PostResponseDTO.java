package com.codiary.backend.global.web.dto.Post;

import com.codiary.backend.global.domain.entity.Comment;
import com.codiary.backend.global.domain.entity.PostFile;
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
import java.util.stream.Collectors;

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

    // 게시글에 댓글 작성하기
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentResultDTO {
        Long commentId;
        Long memberId;
        Long postId;
        String nickname;
        String commentBody;
        LocalDateTime createdAt;
    }

    // 게시글에 대댓글 작성하기
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentReplyResultDTO {
        Long commentId;
        Long memberId;
        Long postId;
        Long parentId;
        String nickname;
        String commentBody;
        LocalDateTime createdAt;
    }

    // 게시글별 댓글 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentListDTO {
        List<PostResponseDTO.CommentDTO> commentList;
        Integer listSize;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentDTO {
        Long commentId;
        Long postId;
        Long memberId;
        String nickname;
        String commentBody;
        LocalDateTime createdAt;
        List<CommentDTO> childCommentList;

        public CommentDTO(Comment comment) {
            this.commentId = comment.getCommentId();
            this.postId = comment.getPost().getPostId();
            this.memberId = comment.getMember().getMemberId();
            this.nickname = comment.getMember().getNickname();
            this.commentBody = comment.getCommentBody();
            this.createdAt = comment.getCreatedAt();
            this.childCommentList = comment.getChildComments().stream()
                    .map(CommentDTO::new)
                    .collect(Collectors.toList());
        }
    }

    // 메인페이지 인기글 전체 리스트 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPopularListDTO {
        List<PostResponseDTO.PostPopularDTO> postPopularList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPopularDTO {
        Long postId;
        Long memberId;
        String nickname;
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

    // 메인페이지 인기글 멤버 관심 카테고리별 리스트 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPopularMemberCategoryListDTO {
        List<PostResponseDTO.PostPopularMemberCategoryDTO> postPopularMemberCategoryList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPopularMemberCategoryDTO {
        Long postId;
        Long memberId;
        String nickname;
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

    // 메인페이지 최신글 리스트 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostLatestListDTO {
        List<PostResponseDTO.PostLatestDTO> postLatestList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostLatestDTO {
        Long postId;
        Long memberId;
        String nickname;
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

    // 메인페이지 팔로잉 게시글 리스트 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostFollowingListDTO {
        List<PostResponseDTO.PostFollowingDTO> postFollowingList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostFollowingDTO {
        Long postId;
        Long memberId;
        String nickname;
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

    // 제목 & 본문 & 저자 & 프로젝트 & 카테고리 검색
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostSearchTitleListDTO {
        List<PostResponseDTO.PostSearchTitleDTO> postSearchTitleList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostSearchTitleDTO {
        Long postId;
        Long memberId;
        String nickname;
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

