package com.codiary.backend.global.web.dto.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryRequestDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SelectCategoryRequestDTO {
        private Long memberId;
        private Long postId;
        private String categoryName;
    }



}
