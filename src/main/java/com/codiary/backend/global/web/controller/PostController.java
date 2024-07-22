package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.web.dto.Post.PostResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RequestMapping("/diaries")
@Slf4j
public class PostController {
    //private final DiaryCommandService diaryCommandService;
    //private final DiaryQueryService diaryQueryService;

    // 글 생성하기
    @PostMapping()
    @Operation(
            summary = "글 생성 API"
            , description = "글을 생성합니다."
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO> createDiary(
    ){
        return null;
    }

    // 글 상세 조회
    @GetMapping("/{diaryId}")
    @Operation(
            summary = "글 상세 조회 API"
            , description = "글을 상세 조회합니다. Param으로 diaryId를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO> findDiary(
    ){
        return null;
    }

    // 글 수정하기
    @PatchMapping("/{diaryId}")
    @Operation(
            summary = "글 수정 API"
            , description = "글을 수정합니다. Param으로 diaryId를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO> updateDiary(
    ){
        return null;
    }

    // 글 삭제하기
    @DeleteMapping("/{diaryId}")
    @Operation(
            summary = "글 삭제 API"
            , description = "글을 삭제합니다. Param으로 diaryId를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO> deleteDiary(
    ){
        return null;
    }



    // 글 공개/비공개 설정
    @PatchMapping("/{diaryId}/visibility")
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
    @PatchMapping("/{diaryId}/customize")
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
    @PatchMapping("/{diaryId}/coauthors")
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
    @PatchMapping("/{diaryId}/team")
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
    @PatchMapping("/{diaryId}/categories")
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
    @GetMapping("/{diaryId}/code-preview")
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
