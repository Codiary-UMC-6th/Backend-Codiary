package com.codiary.backend.domain.team.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.entity.TeamFollow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamFollowRepository extends JpaRepository<TeamFollow, Long>, TeamFollowRepositoryCustom {

    Boolean existsByTeamAndAndMember(Team team, Member member);

    Optional<TeamFollow> findTeamFollowByTeamAndMember(Team team, Member member);
}
