package com.codiary.backend.global.web.dto.Post;

import com.codiary.backend.global.domain.entity.Comment;
import com.codiary.backend.global.domain.enums.PostAccess;
import com.codiary.backend.global.web.dto.PostFile.PostFileResponseDTO;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostResponseDTO {

    @Builder
    public record CreatePostResultDTO(
            Long postId,
            Long memberId,
            Long teamId,
            Long projectId,
            String postTitle,
            String postBody,
            String thumbnailImageUrl,
            Boolean postStatus,
            String postCategory,
            Set<Long> coauthorIds,
            PostAccess postAccess,
            PostFileResponseDTO.PostFileListDTO postFileList
    ) {}

    @Builder
    public record UpdatePostResultDTO(
            Long postId,
            Long memberId,
            Long teamId,
            Long projectId,
            String postTitle,
            String postBody,
            String thumbnailImageUrl,
            Boolean postStatus,
            String postCategory,
            Set<Long> coauthorIds,
            PostAccess postAccess,
            PostFileResponseDTO.PostFileListDTO postFileList
    ) {}

    @Builder
    public record PostDetailDTO(
            Long postId,
            Long memberId,
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
    ) {}

    @Builder
    public record PostPreviewDTO(
            Long postId,
            Long memberId,
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
    ) {}

    @Builder
    public record PostPreviewListDTO(
            List<PostPreviewDTO> posts,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            boolean isFirst,
            boolean isLast
    ) {}

    @Builder
    public record MemberPostPreviewDTO(
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
    ) {}

    @Builder
    public record MemberPostPreviewListDTO(
            List<MemberPostPreviewDTO> posts,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            boolean isFirst,
            boolean isLast
    ) {}

    @Builder
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
    ) {}

    @Builder
    public record TeamPostPreviewListDTO(
            List<TeamPostPreviewDTO> posts,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            boolean isFirst,
            boolean isLast
    ) {}

    @Builder
    public record MemberPostInProjectPreviewDTO(
            Long projectId,
            Long memberId,
            Long postId,
            Long teamId,
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
    ) {}

    @Builder
    public record MemberPostInProjectPreviewListDTO(
            List<MemberPostInProjectPreviewDTO> posts,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            boolean isFirst,
            boolean isLast
    ) {}

    @Builder
    public record TeamPostInProjectPreviewDTO(
            Long projectId,
            Long teamId,
            Long postId,
            Long memberId,
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
    ) {}

    @Builder
    public record TeamPostInProjectPreviewListDTO(
            List<TeamPostInProjectPreviewDTO> posts,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            boolean isFirst,
            boolean isLast
    ) {}

    @Builder
    public record MemberPostInTeamPreviewDTO(
            Long teamId,
            Long memberId,
            Long postId,
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
    ) {}

    @Builder
    public record MemberPostInTeamPreviewListDTO(
            List<MemberPostInTeamPreviewDTO> posts,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            boolean isFirst,
            boolean isLast
    ) {}

    @Builder
    public record PostAdjacentDTO(
            Boolean hasLater,
            Boolean hadOlder,
            PostAdjacentPreviewDTO laterPost,
            PostAdjacentPreviewDTO olderPost
    ) {
        @Builder
        public record PostAdjacentPreviewDTO(
                Long postId,
                Long memberId,
                String nickname,
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
        ) {}
    }

    @Builder
    public record CreateCommentResultDTO(
            Long commentId,
            Long memberId,
            Long postId,
            String nickname,
            String commentBody,
            LocalDateTime createdAt
    ) {}

    @Builder
    public record CreateCommentReplyResultDTO(
            Long commentId,
            Long memberId,
            Long postId,
            Long parentId,
            String nickname,
            String commentBody,
            LocalDateTime createdAt
    ) {}

    @Builder
    public record CommentListDTO(
            List<CommentDTO> commentList,
            Integer listSize
    ) {}

    @Builder
    public record CommentDTO(
            Long commentId,
            Long postId,
            Long memberId,
            String nickname,
            String commentBody,
            LocalDateTime createdAt,
            List<CommentDTO> childCommentList
    ) {
        public CommentDTO(Comment comment) {
            this(
                    comment.getCommentId(),
                    comment.getPost().getPostId(),
                    comment.getMember().getMemberId(),
                    comment.getMember().getNickname(),
                    comment.getCommentBody(),
                    comment.getCreatedAt(),
                    comment.getChildComments().stream()
                            .map(CommentDTO::new)
                            .collect(Collectors.toList())
            );
        }
    }

    @Builder
    public record PostPopularListDTO(
            List<PostPopularDTO> postPopularList,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            Boolean isFirst,
            Boolean isLast
    ) {}

    @Builder
    public record PostPopularDTO(
            Long postId,
            Long memberId,
            String nickname,
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
    ) {}

    @Builder
    public record PostPopularMemberCategoryListDTO(
            List<PostPopularMemberCategoryDTO> postPopularMemberCategoryList,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            Boolean isFirst,
            Boolean isLast
    ) {}

    @Builder
    public record PostPopularMemberCategoryDTO(
            Long postId,
            Long memberId,
            String nickname,
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
    ) {}

    @Builder
    public record PostLatestListDTO(
            List<PostLatestDTO> postLatestList,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            Boolean isFirst,
            Boolean isLast
    ) {}

    @Builder
    public record PostLatestDTO(
            Long postId,
            Long memberId,
            String nickname,
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
    ) {}

    @Builder
    public record PostFollowingListDTO(
            List<PostFollowingDTO> postFollowingList,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            Boolean isFirst,
            Boolean isLast
    ) {}

    @Builder
    public record PostFollowingDTO(
            Long postId,
            Long memberId,
            String nickname,
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
    ) {}

    @Builder
    public record PostSearchTitleListDTO(
            List<PostSearchTitleDTO> postSearchTitleList,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            Boolean isFirst,
            Boolean isLast
    ) {}

    @Builder
    public record PostSearchTitleDTO(
            Long postId,
            Long memberId,
            String nickname,
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
    ) {}
}
