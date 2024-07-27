package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
