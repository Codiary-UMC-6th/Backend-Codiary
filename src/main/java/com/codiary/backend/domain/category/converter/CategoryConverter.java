package com.codiary.backend.domain.category.converter;

import com.codiary.backend.domain.category.dto.CategoryResponseDTO;
import com.codiary.backend.domain.category.entity.Category;
import com.codiary.backend.domain.member.entity.MemberCategory;

import java.util.List;

public class CategoryConverter {
    public static CategoryResponseDTO.MemberCategoryDTO toMemberCategoryDTO(MemberCategory memberCategory) {
        return CategoryResponseDTO.MemberCategoryDTO.builder()
                .memberCategoryId(memberCategory.getMemberCategoryId())
                .categoryName(memberCategory.getCategory().getName())
                .categoryId(memberCategory.getCategory().getCategoryId())
                .build();
    }

    public static List<CategoryResponseDTO.MemberCategoryDTO> toMemberCategoryListDTO(List<MemberCategory> memberCategoryList) {
        return memberCategoryList.stream()
                .map(CategoryConverter::toMemberCategoryDTO)
                .toList();
    }

    public static CategoryResponseDTO.SimpleCategoryDTO toSimpleCategoryDTO(Category category) {
        return CategoryResponseDTO.SimpleCategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getName())
                .build();
    }

    public static List<CategoryResponseDTO.SimpleCategoryDTO> toSimpleCategoryListDTO(List<Category> memberCategoryList) {
        return memberCategoryList.stream()
                .map(CategoryConverter::toSimpleCategoryDTO)
                .toList();
    }
}
