package com.codiary.backend.domain.team.repository;

import com.codiary.backend.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryCustom {
    Optional<Team> findByTeamIdAndDeletedAtIsNull(Long teamId);
}
