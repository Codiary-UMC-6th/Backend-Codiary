package com.codiary.backend.domain.team.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.team.entity.Team;

import java.util.Optional;

public interface TeamRepositoryCustom {
    Optional<Team> findTeamProfile(Long teamId);

    boolean isTeamMember(Team team, Member member);

    Optional<Team> findByIdWithFollowers(Long teamId);
}
