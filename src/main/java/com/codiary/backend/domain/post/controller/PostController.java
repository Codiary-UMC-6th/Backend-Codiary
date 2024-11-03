package com.codiary.backend.domain.post.controller;

import com.codiary.backend.domain.member.service.MemberCommandService;
import com.codiary.backend.domain.post.converter.PostConverter;
import com.codiary.backend.domain.post.dto.response.PostResponseDTO;
import com.codiary.backend.domain.post.dto.request.PostRequestDTO;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.service.PostCommandService;
import com.codiary.backend.domain.post.service.PostService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import com.codiary.backend.global.jwt.JwtTokenProvider;


@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v2/post")
@CrossOrigin
@Slf4j
@Tag(name = "게시글 API", description = "게시글 관련 API입니다.")
public class PostController {
    private final PostService postService;
    private final PostCommandService postCommandService;
    private final MemberCommandService memberCommandService;
    private final JwtTokenProvider jwtTokenProvider;

    // 게시글 생성하기
    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "게시글 생성 API", description = "게시글을 생성합니다. **카테고리 설정은 게시글 생성과는 별도로 설정해야 됩니다.**")
    public ApiResponse<PostResponseDTO.CreatePostResultDTO> createPost(@ModelAttribute PostRequestDTO.CreatePostRequestDTO request) {
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        //Post newPost = postCommandService.createPost(request);
        Post newPost = postCommandService.createPost(request);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toCreateResultDTO(newPost));
    }

    // 멤버의 게시글 수정하기
    @PatchMapping(path = "/{postId}", consumes = "multipart/form-data")
    @Operation(summary = "다이어리 수정 API", description = "다이어리를 수정합니다.")
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> updatePost(@ModelAttribute PostRequestDTO.UpdatePostDTO request, @PathVariable Long postId){
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toUpdatePostResultDTO(postCommandService.updatePost(postId, request)));
    }


    // 게시글 삭제하기
    @DeleteMapping("/{postId}")
    @Operation(summary = "다이어리 삭제 API", description = "다이어리를 삭제합니다.")
    public ApiResponse<?> deletePost(@PathVariable Long postId){
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        postCommandService.deletePost(postId);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, null);
    }











    @Operation(summary = "게시글 검색 결과 페이지네이션", description = "게시글(제목/내용) 키워드 검색 결과를 페이지네이션하여 반환합니다.")
    @GetMapping("/search")
    public ApiResponse<Page<PostResponseDTO.SimplePostResponseDTO>> searchPost(
            @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
            @PageableDefault(size = 9) Pageable pageable) {
        Page<Post> postPage = postService.searchPost(keyword, pageable);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostListResponseDto(postPage));
    }

}
