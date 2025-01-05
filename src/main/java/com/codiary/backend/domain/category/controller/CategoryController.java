package com.codiary.backend.domain.category.controller;

import com.codiary.backend.domain.category.converter.CategoryConverter;
import com.codiary.backend.domain.category.dto.CategoryResponseDTO;
import com.codiary.backend.domain.category.entity.Category;
import com.codiary.backend.domain.category.service.CategoryService;
import com.codiary.backend.domain.member.entity.MemberCategory;
import com.codiary.backend.domain.member.security.CustomMemberDetails;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/category")
@Tag(name = "카테고리 API", description = "카테고리 생성/수정/삭제 API입니다.")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("")
    @Operation(summary = "멤버 카테고리 생성", description = "멤버의 카테고리를 생성합니다.")
    public ApiResponse<CategoryResponseDTO.MemberCategoryDTO> createCategory(@RequestParam(value = "category_name") String categoryName, @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        MemberCategory memberCategory = categoryService.createCategory(categoryName, customMemberDetails.getId());
        return ApiResponse.onSuccess(SuccessStatus.CATEGORY_OK, CategoryConverter.toMemberCategoryDTO(memberCategory));
    }

    @GetMapping("")
    @Operation(summary = "멤버 카테고리 조회", description = "멤버의 카테고리를 조회합니다.")
    public ApiResponse<List<CategoryResponseDTO.MemberCategoryDTO>> getCategoryList(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        List<MemberCategory> memberCategoryList = categoryService.getCategoryList(customMemberDetails.getId());
        return ApiResponse.onSuccess(SuccessStatus.CATEGORY_OK, CategoryConverter.toMemberCategoryListDTO(memberCategoryList));
    }

    @DeleteMapping("/{member_category_id}")
    @Operation(summary = "멤버 카테고리 삭제", description = "멤버의 카테고리를 삭제합니다.")
    public ApiResponse<String> deleteCategory(@PathVariable("member_category_id") Long memberCategoryId, @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        categoryService.deleteCategory(memberCategoryId, customMemberDetails.getId());
        return ApiResponse.onSuccess(SuccessStatus.CATEGORY_OK, "카테고리 삭제가 완료되었습니다.");
    }

    @Operation(summary = "카테고리 검색", description = "카테고리를 검색합니다.")
    @GetMapping("/search")
    public ApiResponse<List<CategoryResponseDTO.SimpleCategoryDTO>> searchCategory(
            @RequestParam(value = "category", defaultValue = "", required = false) String category,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
            ) {
        Long memberId = memberDetails.getId();
        List<Category> categories = categoryService.searchCategory(memberId, category);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, CategoryConverter.toSimpleCategoryListDTO(categories));
    }
}
