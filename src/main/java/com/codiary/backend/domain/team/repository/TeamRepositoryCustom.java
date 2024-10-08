package com.codiary.backend.domain.team.repository;

import com.codiary.backend.domain.team.entity.Team;

import java.util.Optional;

public interface TeamRepositoryCustom {
    Optional<Team> findTeamProfile(Long teamId);
}
