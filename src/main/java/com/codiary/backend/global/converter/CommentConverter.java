package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Comment;
import com.codiary.backend.global.web.dto.Comment.CommentResponseDTO;

public class CommentConverter {

    // 댓글 수정하기
    // nickname 관련 could not initialize proxy - no Session 에러
    public static CommentResponseDTO.PatchCommentResultDTO toPatchCommentResultDTO(Comment comment) {
        return CommentResponseDTO.PatchCommentResultDTO.builder()
                .commentId(comment.getCommentId())
                .memberId(comment.getMember().getMemberId())
                .postId(comment.getPost().getPostId())
                .nickname(comment.getMember().getNickname())
                .commentBody(comment.getCommentBody())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    // 댓글 삭제하기
    public static CommentResponseDTO.DeleteCommentResultDTO toDeleteCommentResultDTO(Long commentId) {
        return CommentResponseDTO.DeleteCommentResultDTO.builder()
                .commentId(commentId)
                .build();
    }

    // 게시글별 댓글 개수 조회
    public static CommentResponseDTO.CountCommentsResultDTO toCountCommentsResultDTO(Long postId, int countComments) {
        return CommentResponseDTO.CountCommentsResultDTO.builder()
                .postId(postId)
                .countComments(countComments)
                .build();
    }

}
