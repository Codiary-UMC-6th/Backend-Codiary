package com.codiary.backend.domain.comment.controller;

import com.codiary.backend.domain.comment.converter.CommentConverter;
import com.codiary.backend.domain.comment.dto.request.CommentRequestDTO;
import com.codiary.backend.domain.comment.dto.response.CommentResponseDTO;
import com.codiary.backend.domain.comment.entity.Comment;
import com.codiary.backend.domain.comment.service.ReplyService;
import com.codiary.backend.domain.member.security.CustomMemberDetails;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2")
@Tag(name = "대댓글 API", description = "대댓글 관련 API 입니다.")
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "대댓글 달기")
    @PostMapping("comment/{comment_id}/reply")
    public ApiResponse<CommentResponseDTO.CommentDTO> replyToComment(
            @PathVariable("comment_id") Long commentId,
            @RequestBody CommentRequestDTO.CommentDTO request,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long replierId = memberDetails.getId();
        Comment newReply = replyService.replyToComment(commentId, replierId, request);
        return ApiResponse.onSuccess(SuccessStatus.COMMENT_OK, CommentConverter.toCommentResponseDto(newReply));
    }

    @Operation(summary = "대댓글 수정")
    @PostMapping("reply/{reply_id}")
    public ApiResponse<CommentResponseDTO.CommentDTO> updateReply(
            @PathVariable("reply_id") Long replyId,
            @RequestBody CommentRequestDTO.CommentDTO request,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long replierId = memberDetails.getId();
        Comment updatedReply = replyService.updateReply(replyId, replierId, request);
        return ApiResponse.onSuccess(SuccessStatus.COMMENT_OK, CommentConverter.toCommentResponseDto(updatedReply));
    }

    @Operation(summary = "대댓글 삭제")
    @DeleteMapping("reply/{reply_id}")
    public ApiResponse<String> deleteReply(
            @PathVariable("reply_id") Long replyId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long replierId = memberDetails.getId();
        String response = replyService.deleteReply(replyId, replierId);
        return ApiResponse.onSuccess(SuccessStatus.COMMENT_OK, response);
    }

    @Operation(summary = "대댓글 조회")
    @GetMapping("comment/{comment_id}")
    public ApiResponse<List<CommentResponseDTO.CommentDTO>> getReplyList(
            @PathVariable("comment_id") Long commentId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Long requesterId = memberDetails.getId();
        List<Comment> replies = replyService.getReplies(commentId, requesterId, pageable);
        return ApiResponse.onSuccess(SuccessStatus.COMMENT_OK, CommentConverter.toCommentResponseListDto(replies));
    }
}
