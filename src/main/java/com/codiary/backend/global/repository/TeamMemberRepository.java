package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.mapping.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
  Optional<TeamMember> findByTeamAndMember(Team team, Member member);

  Optional<TeamMember> findByTeam_TeamIdAndMember_MemberId(Long teamId, Long memberId);
}