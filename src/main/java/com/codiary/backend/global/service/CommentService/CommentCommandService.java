package com.codiary.backend.global.service.CommentService;

import com.codiary.backend.global.domain.entity.Comment;
import com.codiary.backend.global.web.dto.Comment.CommentRequestDTO;
import com.codiary.backend.global.web.dto.Comment.CommentResponseDTO;

public interface CommentCommandService {

    // 댓글 수정하기
//    Comment patchComment(Long commentId, CommentRequestDTO.PatchCommentDTO request);
    CommentResponseDTO.PatchCommentResultDTO patchComment(Long commentId, CommentRequestDTO.PatchCommentDTO request);

    // 댓글 삭제하기
    void deleteComment(Long commentId);

    // 게시글의 댓글 개수 조회
    int countComments(Long postId);

}
