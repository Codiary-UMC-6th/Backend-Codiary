package com.codiary.backend.global.service.CategoryService;

import com.codiary.backend.global.domain.entity.mapping.Categories;

import java.util.List;

public interface CategoryQueryService {
    List<Categories> getCategories();
    boolean categoryExists(String categoryName);
}
