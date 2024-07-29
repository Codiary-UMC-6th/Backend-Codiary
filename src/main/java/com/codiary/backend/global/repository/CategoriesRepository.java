package com.codiary.backend.global.repository;


import com.codiary.backend.global.domain.entity.mapping.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    Optional<Categories> findByName(String name);
    boolean existsByName(String name);
}
