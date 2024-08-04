package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOrderByProjectIdDesc();
}
