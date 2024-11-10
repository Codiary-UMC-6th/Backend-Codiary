package com.codiary.backend.domain.comment.controller;

import com.codiary.backend.domain.comment.dto.request.CommentRequestDTO;
import com.codiary.backend.domain.comment.entity.Comment;
import com.codiary.backend.domain.comment.service.ReplyService;
import com.codiary.backend.domain.member.security.CustomMemberDetails;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2")
@Tag(name = "대댓글 API", description = "대댓글 관련 API 입니다.")
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "대댓글 달기")
    @PostMapping("comments/{comment_id}/replys")
    public ApiResponse<?> replyToComment(
            @PathVariable("comment_id") Long commentId,
            @RequestBody CommentRequestDTO.CommentDTO request,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long replierId = memberDetails.getId();
        Comment newComment = replyService.replyToComment(commentId, replierId, request);
        return ApiResponse.onSuccess(SuccessStatus.COMMENT_OK, null);
    }
}
