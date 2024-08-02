package com.codiary.backend.global.web.controller;


import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.CategoryConverter;
import com.codiary.backend.global.converter.MemberCategoryConverter;
import com.codiary.backend.global.domain.entity.mapping.Categories;
import com.codiary.backend.global.domain.entity.mapping.MemberCategory;
import com.codiary.backend.global.service.CategoryService.CategoryCommandService;
import com.codiary.backend.global.service.CategoryService.CategoryQueryService;
import com.codiary.backend.global.web.dto.Category.CategoryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

    private final CategoryQueryService categoryQueryService;

    // 키워드 입력을 통해 관련 카테고리 리스트 조회
    @GetMapping("/list")
    @Operation(summary = "키워드 입력을 통해 관련 카테고리 리스트 조회 API", description = "키워드 입력을 통해 관련 카테고리 리스트 조회합니다. 입력한 키워드가 포함된 모든 카테고리를 리스트로 조회할 수 있습니다. Param으로 키워드를 입력하세요."
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<CategoryResponseDTO.CategoryPreviewListDTO> findCategoriesByKeyword(@RequestParam Optional<String> search){
        List<Categories> categories = categoryQueryService.getCategoriesByKeyword(search);
        return ApiResponse.onSuccess(SuccessStatus.CATEGORY_OK, CategoryConverter.toCategoryPreviewListDTO(categories));
    }

}
