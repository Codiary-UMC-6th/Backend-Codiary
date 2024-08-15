package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.MemberCategoryConverter;
import com.codiary.backend.global.domain.entity.mapping.MemberCategory;
import com.codiary.backend.global.service.CategoryService.CategoryCommandService;
import com.codiary.backend.global.service.MemberCategoryService.MemberCategoryCommandService;
import com.codiary.backend.global.web.dto.Category.CategoryResponseDTO;
import com.codiary.backend.global.web.dto.MemberCategory.MemberCategoryRequestDTO;
import com.codiary.backend.global.web.dto.MemberCategory.MemberCategoryResponseDTO;
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
@RequestMapping("/membercategories")
@Slf4j
@Tag(name = "회원별 관심 카테고리 API", description = "회원별 관심 카테고리 추가/수정/삭제 관련 API입니다.")
public class MemberCategoryController {

    private final MemberCategoryCommandService memberCategoryCommandService;

    // 스프링 시큐리티 완료되면 전체적으로 로그인 회원 관련 수정해야 함

    // 카테고리를 회원별 관심 카테고리에 추가
    @PostMapping("/add/{memberId}/{categoryId}")
    @Operation(
            summary = "회원별 관심 카테고리탭 추가 API",
            description = "membercategory, memberId, categoryId"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBERCATEGORY8000", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "memberId", description = "회원 고유번호, path variable 입니다!"),
            @Parameter(name = "categoryId", description = "카테고리 고유번호, path variable 입니다!")
    })
    public ApiResponse<MemberCategoryResponseDTO.CreateMemberCategoryResultDTO> createMemberCategory(
            @PathVariable(name = "memberId") Long memberId,
            @PathVariable(name = "categoryId") Long categoryId
    ) {

        MemberCategory memberCategory = memberCategoryCommandService.createMemberCategory(memberId, categoryId);

        return ApiResponse.onSuccess(SuccessStatus.MEMBERCATEGORY_OK, MemberCategoryConverter.toCreateMemberCategoryResultDTO(memberCategory));

    }

    // 회원별 관심 카테고리탭 수정
    @PatchMapping("/patch/{memberCategoryId}")
    @Operation(
            summary = "회원별 관심 카테고리탭 수정 API",
            description = "membercategory, memberCategoryId"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBERCATEGORY8000", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "memberCategoryId", description = "회원별 관심 카테고리 고유번호, path variable 입니다!")
    })
    public ApiResponse<MemberCategoryResponseDTO.PatchMemberCategoryResultDTO> patchMemberCategory(
            @PathVariable(name = "memberCategoryId") Long memberCategoryId,
            @RequestBody MemberCategoryRequestDTO.PatchMemberCategoryResultDTO request
    ) {

        MemberCategory memberCategory = memberCategoryCommandService.patchMemberCategory(memberCategoryId, request);

        return ApiResponse.onSuccess(SuccessStatus.MEMBERCATEGORY_OK, MemberCategoryConverter.toPatchMemberCategoryResultDTO(memberCategory));

    }

    // 회원별 관심 카테고리탭 삭제하기
    @DeleteMapping("/delete/{memberCategoryId}")
    @Operation(
            summary = "회원별 관심 카테고리탭 삭제 API",
            description = "membercategory, memberCategoryId"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBERCATEGORY8000", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "memberCategoryId", description = "회원별 관심 카테고리 고유번호, path variable 입니다!")
    })
    public ApiResponse<MemberCategoryResponseDTO.DeleteMemberCategoryResultDTO> deleteMemberCategory(
            @PathVariable(name = "memberCategoryId") Long memberCategoryId
    ) {

        memberCategoryCommandService.deleteMemberCategory(memberCategoryId);

        return ApiResponse.onSuccess(SuccessStatus.MEMBERCATEGORY_OK, MemberCategoryConverter.toDeleteMemberCategoryResultDTO(memberCategoryId));

    }

}
