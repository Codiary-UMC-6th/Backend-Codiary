package com.codiary.backend.global.service.CategoryService;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.mapping.Categories;
import com.codiary.backend.global.repository.CategoryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@Getter
public class CategoryCommandServiceImpl implements CategoryCommandService{

    private final CategoryRepository categoriesRepository;

    @Transactional
    @Override
    public Categories addCategory(Post post, String categoryName) {
        return categoriesRepository.findByName(categoryName)
                .orElseGet(() -> categoriesRepository.save(Categories.builder()
                        .name(categoryName)
                        .build()));
    }
}
