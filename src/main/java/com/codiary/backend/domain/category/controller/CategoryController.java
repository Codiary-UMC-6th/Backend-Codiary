package com.codiary.backend.domain.category.controller;

import com.codiary.backend.domain.category.converter.CategoryConverter;
import com.codiary.backend.domain.category.service.CategoryService;
import com.codiary.backend.domain.member.entity.MemberCategory;
import com.codiary.backend.domain.member.security.CustomMemberDetails;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/category")
@Tag(name = "Category", description = "카테고리 API")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("")
    public ApiResponse<?> createCategory(@RequestParam(value = "category_name") String categoryName, @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        MemberCategory memberCategory = categoryService.createCategory(categoryName, customMemberDetails.getId());
        return ApiResponse.onSuccess(SuccessStatus.CATEGORY_OK, CategoryConverter.toMemberCategoryDTO(memberCategory));
    }
}
