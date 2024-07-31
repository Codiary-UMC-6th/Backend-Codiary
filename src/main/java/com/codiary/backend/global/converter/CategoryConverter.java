package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.mapping.Categories;
import com.codiary.backend.global.web.dto.Category.CategoryResponseDTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CategoryConverter {

    public static CategoryResponseDTO.CategoryPreviewDTO toCategoryPreviewDTO(Categories categories) {
        return CategoryResponseDTO.CategoryPreviewDTO.builder()
                .categoryId(categories.getCategoryId())
                .categoryName(categories.getName())
                .build();
    }

    public static CategoryResponseDTO.CategoryPreviewListDTO toCategoryPreviewListDTO(List<Categories> categoriesList) {
        List<CategoryResponseDTO.CategoryPreviewDTO> categoryPreviewDTOList = IntStream.range(0, categoriesList.size())
                .mapToObj(i->toCategoryPreviewDTO(categoriesList.get(i)))
                .collect(Collectors.toList());
        return CategoryResponseDTO.CategoryPreviewListDTO.builder()
                .categories(categoryPreviewDTOList)
                .build();
    }
}
