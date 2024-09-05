package com.codiary.backend.global.web.dto.Comment;

import lombok.Builder;

import java.time.LocalDateTime;

public class CommentResponseDTO {

    //댓글 수정하기 응답 DTO
    @Builder
    public record PatchCommentResultDTO(Long commentId, Long memberId, Long postId, String nickname, String commentBody, LocalDateTime createdAt, LocalDateTime updatedAt) {}

    // 댓글 삭제하기 응답 DTO
    @Builder
    public record DeleteCommentResultDTO(Long commentId) {}

    // 댓글 개수 조회 응답  DTO
    @Builder
    public record CountCommentsResultDTO(Long postId, int countComment) {}

}
