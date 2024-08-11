package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long > {
}
