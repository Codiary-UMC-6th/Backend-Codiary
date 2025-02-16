package com.codiary.backend.domain.team.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.entity.TeamMember;
import com.codiary.backend.domain.team.enumerate.TeamMemberRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    boolean existsByTeamAndMember(Team team, Member member);

    Optional<TeamMember> findByTeamAndMember(Team team, Member member);

    Long countTeamMembersByTeam(Team team);

    Long countTeamMembersByTeamAndTeamMemberRole(Team team, TeamMemberRole teamMemberRole);

    List<TeamMember> findTeamMembersByTeam(Team team);

    List<TeamMember> findByMember(Member member);
}
