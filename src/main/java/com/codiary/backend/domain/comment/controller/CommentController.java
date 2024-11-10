package com.codiary.backend.domain.comment.controller;

import com.codiary.backend.domain.comment.converter.CommentConverter;
import com.codiary.backend.domain.comment.dto.request.CommentRequestDTO.CommentDTO;
import com.codiary.backend.domain.comment.dto.response.CommentResponseDTO;
import com.codiary.backend.domain.comment.entity.Comment;
import com.codiary.backend.domain.comment.service.CommentService;
import com.codiary.backend.domain.member.security.CustomMemberDetails;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2")
@Tag(name = "댓글 API", description = "댓글 관련 API 입니다.")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 달기")
    @PostMapping("/posts/{post_id}/comments")
    public ApiResponse<CommentResponseDTO.CommentDTO> commentOnPost(
            @PathVariable("post_id") Long postId,
            @RequestBody CommentDTO request,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long commenterId = memberDetails.getId();
        Comment newComment = commentService.commentOnPost(postId, commenterId, request);
        return ApiResponse.onSuccess(SuccessStatus.COMMENT_OK, CommentConverter.toCommentResponseDto(newComment));
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("comments/{comment_id}")
    public ApiResponse<String> deleteComment(
            @PathVariable("comment_id") Long commentId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        String response = commentService.deleteComment(commentId, memberId);
        return ApiResponse.onSuccess(SuccessStatus.COMMENT_OK, response);
    }

    @Operation(summary = "댓글 수정")
    @PatchMapping("comments/{comment_id}")
    public ApiResponse<CommentResponseDTO.CommentDTO> updateComment(
            @PathVariable("comment_id") Long commentId,
            @RequestBody CommentDTO request,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        Comment updatedComment = commentService.updateComment(commentId, memberId, request);
        return ApiResponse.onSuccess(SuccessStatus.COMMENT_OK, CommentConverter.toCommentResponseDto(updatedComment));
    }

    @Operation(summary = "댓글 조회")
    @GetMapping("/posts/{post_id}/comments")
    public ApiResponse<List<CommentResponseDTO.CommentDTO>> getComments(
            @PathVariable("post_id") Long postId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Long memberId = memberDetails.getId();
        List<Comment> comments = commentService.getComments(postId, memberId, pageable);
        return ApiResponse.onSuccess(SuccessStatus.COMMENT_OK, CommentConverter.toCommentResponseListDto(comments));
    }
}
