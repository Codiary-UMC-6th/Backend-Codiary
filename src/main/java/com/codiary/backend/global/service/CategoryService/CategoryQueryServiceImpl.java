package com.codiary.backend.global.service.CategoryService;

import com.codiary.backend.global.domain.entity.mapping.Categories;
import com.codiary.backend.global.repository.CategoryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@Getter
public class CategoryQueryServiceImpl implements CategoryQueryService{

    private final CategoryRepository categoriesRepository;

    @Override
    public List<Categories> getCategoriesByKeyword(Optional<String> optSearch) {
        if (optSearch.isPresent()) {
            String search = optSearch.get();
            return categoriesRepository.findAllByNameContainingIgnoreCaseOrderByCategoryIdDesc(search);
        }
        return categoriesRepository.findAllByOrderByCategoryIdDesc();
    }

    @Override
    public boolean categoryExists(String categoryName) {
        return categoriesRepository.existsByName(categoryName);
    }

}
