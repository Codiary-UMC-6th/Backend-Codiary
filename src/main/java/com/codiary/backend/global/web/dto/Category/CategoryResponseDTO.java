package com.codiary.backend.global.web.dto.Category;

import lombok.Builder;

import java.util.List;

public class CategoryResponseDTO {

    @Builder
    public record CategoryPreviewDTO(Long categoryId, String categoryName) {}

    @Builder
    public record CategoryPreviewListDTO(List<CategoryPreviewDTO> categories) {}
}
