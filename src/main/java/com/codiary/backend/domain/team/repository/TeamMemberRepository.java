package com.codiary.backend.domain.team.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.entity.TeamMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    boolean existsByTeamAndMember(Team team, Member member);

    Optional<TeamMember> findByTeamAndMember(Team team, Member member);

    Long countTeamMembersByTeam(Team team);
}
