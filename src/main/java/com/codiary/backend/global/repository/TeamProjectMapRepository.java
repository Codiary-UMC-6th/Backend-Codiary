package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.mapping.TeamProjectMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TeamProjectMapRepository extends JpaRepository<TeamProjectMap, Long> {
}