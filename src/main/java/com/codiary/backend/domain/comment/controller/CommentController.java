package com.codiary.backend.domain.comment.controller;

import com.codiary.backend.domain.comment.service.CommentService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/posts/{post_id}/comments")
@Tag(name = "댓글 API", description = "댓글 관련 API 입니다.")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 달기")
    @PostMapping()
    public ApiResponse<?> commentPost(@PathVariable("post_id") Long postId) {
        return null;
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{comment_id}")
    public ApiResponse<?> deleteComment(
            @PathVariable("post_id") Long postId,
            @PathVariable("comment_id") Long commentId
    ) {
        return null;
    }

    @Operation(summary = "댓글 조회", description = "기본적으로 10개씩 페이지네이션 하여 제공됩니다.")
    @GetMapping("/{comment_id}")
    public ApiResponse<?> getComments(
            @PathVariable("post_id") Long postId,
            @PathVariable("comment_id") Long commentId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return null;
    }
}
