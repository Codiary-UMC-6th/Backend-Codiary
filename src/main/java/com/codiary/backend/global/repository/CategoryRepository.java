package com.codiary.backend.global.repository;


import com.codiary.backend.global.domain.entity.mapping.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Categories, Long> {
    Optional<Categories> findByName(String name);
    List<Categories> findAllByNameContainingIgnoreCaseOrderByCategoryIdDesc(String keyword);
    List<Categories> findAllByOrderByCategoryIdDesc();
    boolean existsByName(String name);
}
