package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.BookmarkConverter;
import com.codiary.backend.global.domain.entity.Bookmark;
import com.codiary.backend.global.service.BookmarkService.BookmarkCommandService;
import com.codiary.backend.global.web.dto.Bookmark.BookmarkResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
@Slf4j
@Tag(name = "북마크 API", description = "북마크 추가/삭제/개수 조회 관련 API입니다.")
public class BookmarkController {

    private final BookmarkCommandService bookmarkCommandService;

    // 스프링 시큐리티 완료되면 전체적으로 로그인 회원 관련 수정해야 함

    // 북마크 추가
    @PostMapping("/add/{memberId}/{postId}")
    @Operation(
            summary = "북마크 추가 API",
            description = "bookmarks, memberId, postId"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOOKMARK6000", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "memberId", description = "회원 고유번호, path variable 입니다!"),
            @Parameter(name = "postId", description = "게시글 고유번호, path variable 입니다!")
    })
    public ApiResponse<BookmarkResponseDTO.CreateBookmarkResultDTO> createBookmark(
            @PathVariable(name = "memberId") Long memberId,
            @PathVariable(name = "postId") Long postId
    ) {

        Bookmark bookmark = bookmarkCommandService.createBookmark(memberId, postId);

        return ApiResponse.onSuccess(SuccessStatus.BOOKMARK_OK, BookmarkConverter.toCreateBookmarkResultDTO(bookmark));

    }

    // 북마크 삭제
    @DeleteMapping("/delete/{bookmarkId}")
    @Operation(
            summary = "북마크 삭제 API",
            description = "bookmarks, bookmarkId"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOOKMARK6000", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "bookmarkId", description = "북마크 고유번호, path variable 입니다!")
    })
    public ApiResponse<BookmarkResponseDTO.DeleteBookmarkResultDTO> deleteBookmark(
            @PathVariable(name = "bookmarkId") Long bookmarkId
    ) {

        bookmarkCommandService.deleteBookmark(bookmarkId);

        return ApiResponse.onSuccess(SuccessStatus.BOOKMARK_OK, BookmarkConverter.toDeleteBookmarkResultDTO(bookmarkId));

    }

    // 게시글의 북마크 개수 조회
    // 얘는 회원 인증 필요 ㄴㄴ
    @GetMapping("/count/{postId}")
    @Operation(
            summary = "게시글의 북마크 개수 조회 API",
            description = "bookmarks, postId"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOOKMARK6000", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 고유번호, path variable 입니다!")
    })
    public ApiResponse<BookmarkResponseDTO.CountBookmarkResultDTO> countBookmark(
            @PathVariable(name = "postId") Long postId
    ) {

        int countBookmark = bookmarkCommandService.countBookmark(postId);

        return ApiResponse.onSuccess(SuccessStatus.BOOKMARK_OK, BookmarkConverter.toCountBookmarkResultDTO(postId, countBookmark));

    }

}
