package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.domain.entity.TeamFollow;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.repository.TeamFollowRepository;
import com.codiary.backend.global.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamFollowService {

  private final TeamFollowRepository teamFollowRepository;
  private final TeamRepository teamRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public TeamFollow followTeam(Long teamId, Member fromMember) {
    if (fromMember == null) {
      throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
    }

    Team team = teamRepository.findById(teamId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

    TeamFollow teamFollow = teamFollowRepository.findByMemberAndTeam(fromMember, team)
        .orElse(null);

    if (teamFollow == null) {
      teamFollow = TeamFollow.builder()
          .member(fromMember)
          .team(team)
          .followStatus(true)
          .build();
      fromMember.getFollowedTeams().add(teamFollow);
    } else {
      if (teamFollow.getFollowStatus()) {
        teamFollow.updateFollowStatus(false);
        fromMember.getFollowedTeams().remove(teamFollow);
      } else {
        teamFollow.updateFollowStatus(true);
        fromMember.getFollowedTeams().add(teamFollow);
      }

      // 지연 로딩된 컬렉션 또는 엔티티 초기화
      fromMember.getFollowedTeams().size();
      Hibernate.initialize(teamFollow.getMember());
    }

    teamFollowRepository.save(teamFollow);

    return teamFollow;
  }

  @Transactional(readOnly = true)
  public Boolean isFollowingTeam(Long teamId, Member fromMember) {
    if (fromMember == null) {
      throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
    }

    Team team = teamRepository.findById(teamId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

    return teamFollowRepository.findByMemberAndTeam(fromMember, team)
        .map(TeamFollow::getFollowStatus)
        .orElse(false);
  }
}