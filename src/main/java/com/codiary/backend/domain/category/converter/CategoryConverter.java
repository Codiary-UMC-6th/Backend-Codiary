package com.codiary.backend.domain.category.converter;

import com.codiary.backend.domain.category.dto.CategoryResponseDTO;
import com.codiary.backend.domain.member.entity.MemberCategory;

public class CategoryConverter {
    public static CategoryResponseDTO.MemberCategoryDTO toMemberCategoryDTO(MemberCategory memberCategory) {
        return CategoryResponseDTO.MemberCategoryDTO.builder()
                .memberCategoryId(memberCategory.getMemberCategoryId())
                .categoryName(memberCategory.getCategory().getName())
                .categoryId(memberCategory.getCategory().getCategoryId())
                .build();
    }
}
