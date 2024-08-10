package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.CommentConverter;
import com.codiary.backend.global.domain.entity.Comment;
import com.codiary.backend.global.service.CommentService.CommentCommandService;
import com.codiary.backend.global.web.dto.Comment.CommentRequestDTO;
import com.codiary.backend.global.web.dto.Comment.CommentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
//@Validated
//@CrossOrigin
@RequestMapping("/comments")
@Slf4j
public class CommentController {

    private final CommentCommandService commentCommandService;

    // 댓글 수정하기
    @PatchMapping("/patch/{commentId}")
    @Operation(
            summary = "댓글 수정 API",
            description = "comment, commentId"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4000", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "commentId", description = "댓글 고유번호, path variable 입니다!")
    })
    public ApiResponse<CommentResponseDTO.PatchCommentResultDTO> patchComment(
            @PathVariable(name = "commentId") Long commentId,
            @RequestBody CommentRequestDTO.PatchCommentDTO request
    ) {

        Comment comment = commentCommandService.patchComment(commentId, request);

        return ApiResponse.onSuccess(SuccessStatus.COMMENT_OK, CommentConverter.toPatchCommentResultDTO(comment));

    }

    // 댓글 삭제하기
    @DeleteMapping("/delete/{commentId}")
    @Operation(
            summary = "댓글 삭제 API",
            description = "comment, commentId"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4000", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "commentId", description = "댓글 고유번호, path variable 입니다!")
    })
    public ApiResponse<CommentResponseDTO.DeleteCommentResultDTO> deleteComment(
            @PathVariable(name = "commentId") Long commentId
    ) {

        commentCommandService.deleteComment(commentId);

        return ApiResponse.onSuccess(SuccessStatus.COMMENT_OK, CommentConverter.toDeleteCommentResultDTO(commentId));

    }

    // 게시글의 댓글 개수 조회
    @GetMapping("/count/{postId}")
    @Operation(
            summary = "게시글의 댓글 개수 조회 API",
            description = "comment, postId"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4000", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 고유번호, path variable 입니다!")
    })
    public ApiResponse<CommentResponseDTO.CountCommentsResultDTO> countComments(
            @PathVariable(name = "postId") Long postId
    ) {

        int countComments = commentCommandService.countComments(postId);

        return ApiResponse.onSuccess(SuccessStatus.COMMENT_OK, CommentConverter.toCountCommentsResultDTO(postId, countComments));

    }

}
