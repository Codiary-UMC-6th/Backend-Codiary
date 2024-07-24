package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.PostConverter;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.service.PostService.PostCommandService;
import com.codiary.backend.global.service.PostService.PostQueryService;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;
import com.codiary.backend.global.web.dto.Post.PostResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RequestMapping("/posts")
@Slf4j
public class PostController {
    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    // 글 생성하기
    @PostMapping()
    @Operation(
            summary = "글 생성 API"
            , description = "글을 생성합니다."
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO.CreatePostResultDTO> createPost(
            @RequestParam Long memberId,
            @RequestBody PostRequestDTO.CreatePostRequestDTO request
            ){
        Long teamId = 1L;
        Post newPost = postCommandService.createPost(memberId, teamId, request);
        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toCreateResultDTO(newPost)
        );
    }

    // 글 상세 조회
    @GetMapping("/{postId}")
    @Operation(
            summary = "글 상세 조회 API"
            , description = "글을 상세 조회합니다. Param으로 Id를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO> findPost(
    ){
        return null;
    }

    // 제목의 키워드로 멤버가 작성한 모든 글 리스트 조회
    @GetMapping("/lists/{memberId}")
    @Operation(
            summary = "제목의 키워드로 멤버가 작성한 글 리스트 조회 API"
            , description = "키워드를 통해 멤버가 작성한 모든 글의 리스트를 조회합니다. Param으로 키워드를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<?> findPostList(
            @RequestParam Optional<String> search
    ){
        List<Post> Posts = postQueryService.findAllBySearch(search);
        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toPostPreviewListDTO(Posts)
        );
    }

    // 글 수정하기
    @PatchMapping("/{postId}")
    @Operation(
            summary = "글 수정 API"
            , description = "글을 수정합니다. Param으로 Id를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> updatePost(
            @RequestParam Long memberId,
            @RequestBody PostRequestDTO.UpdatePostDTO request,
            @PathVariable Long postId
    ){
        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.UpdatePostResultDTO(
                        postCommandService.updatePost(memberId, postId, request)
                )
        );
    }

    // 글 삭제하기
    @DeleteMapping("/{postId}")
    @Operation(
            summary = "글 삭제 API"
            , description = "글을 삭제합니다. Param으로 Id를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<?> deletePost(
            @RequestParam Long memberId,
            @PathVariable Long postId
    ){
        postCommandService.deletePost(memberId, postId);
        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                null
        );
    }



    // 글 공개/비공개 설정
    @PatchMapping("/visibility/{postId}")
    @Operation(
            summary = "글 공개/비공개 API"
            , description = "글의 공개/비공개 유무를 설정합니다. Param으로 diaryId를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO> setDiaryVisibility(
    ){
        return null;
    }

    // 글 커스터마이징 옵션 변경
    @PatchMapping("/customize/{postId}")
    @Operation(
            summary = "글 커스터마이징 옵션 변경 API"
            , description = "글의 커스터마이징 옵션을 변경합니다. Param으로 diaryId를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO> customizeDiary(
    ){
        return null;
    }

    // 글 공동 저자 설정
    @PatchMapping("/coauthors/{postId}")
    @Operation(
            summary = "글 공동 저자 설정 API"
            , description = "글의 공동 저자를 설정합니다. Param으로 diaryId를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO> setDiaryCoauthor(
    ){
        return null;
    }

    // 글의 소속 팀 설정
    @PatchMapping("/team/{postId}")
    @Operation(
            summary = "글의 소속 팀 설정 API"
            , description = "글의 소속 팀을 설정합니다. Param으로 diaryId를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO> setDiaryTeam(
    ){
        return null;
    }

    // 글의 카테고리 및 키워드 설정
    @PatchMapping("/categories/{postId}")
    @Operation(
            summary = "글의 카테고리 및 키워드 설정 API"
            , description = "글의 카테고리 및 키워드를 설정합니다. Param으로 diaryId를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO> setDiaryCategory(
    ){
        return null;
    }

    // AI로 코드 실행 미리보기
    @GetMapping("/code-preview/{postId}")
    @Operation(
            summary = "AI로 코드 실행 미리보기 API"
            , description = "AI로 특정 글에 포함된 코드를 실행하여 미리보기 결과를 반환합니다. Param으로 diaryId를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO> showDiaryCodePreview(
    ){
        return null;
    }

}
