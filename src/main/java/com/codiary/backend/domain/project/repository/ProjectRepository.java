package com.codiary.backend.domain.project.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {

    List<Project> findAllByOrderByProjectIdDesc();

    Optional<Project> findByProjectNameAndDeletedAtIsNull(String projectName);
}
