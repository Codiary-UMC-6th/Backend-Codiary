package com.codiary.backend.global.service.CategoryService;

import com.codiary.backend.global.domain.entity.mapping.Categories;

import java.util.List;
import java.util.Optional;

public interface CategoryQueryService {
    List<Categories> getCategoriesByKeyword(Optional<String> optSearch);
    boolean categoryExists(String categoryName);
}
