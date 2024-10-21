package com.codiary.backend.domain.post.controller;

import com.codiary.backend.domain.post.converter.PostConverter;
import com.codiary.backend.domain.post.dto.response.PostResponseDTO;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.service.PostService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/post")
@Tag(name = "게시글 API", description = "게시글 관련 API입니다.")
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글 검색 결과 페이지네이션", description = "게시글(제목/내용) 키워드 검색 결과를 페이지네이션하여 반환합니다.")
    @GetMapping("/search")
    public ApiResponse<Page<PostResponseDTO.SimplePostResponseDTO>> searchPost(
            @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
            @PageableDefault(size = 9) Pageable pageable) {
        Page<Post> postPage = postService.searchPost(keyword, pageable);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostListResponseDto(postPage));
    }

}
