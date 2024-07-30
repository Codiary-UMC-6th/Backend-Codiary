package com.codiary.backend.global.service.CategoryService;

import com.codiary.backend.global.domain.entity.mapping.Categories;
import com.codiary.backend.global.repository.CategoriesRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@Getter
public class CategoryQueryServiceImpl implements CategoryQueryService{

    private final CategoriesRepository categoriesRepository;

    @Override
    public List<Categories> getCategories() {
        return categoriesRepository.findAll();
    }

    @Override
    public boolean categoryExists(String categoryName) {
        return categoriesRepository.existsByName(categoryName);
    }

}
