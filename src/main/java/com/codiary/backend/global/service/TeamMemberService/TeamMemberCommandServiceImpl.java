package com.codiary.backend.global.service.TeamMemberService;

import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.domain.entity.mapping.TeamMember;
import com.codiary.backend.global.domain.enums.MemberRole;
import com.codiary.backend.global.jwt.SecurityUtil;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.repository.TeamMemberRepository;
import com.codiary.backend.global.repository.TeamRepository;
import com.codiary.backend.global.web.dto.TeamMember.TeamMemberRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TeamMemberCommandServiceImpl implements TeamMemberCommandService {

  private final TeamRepository teamRepository;
  private final MemberRepository memberRepository;
  private final TeamMemberRepository teamMemberRepository;

  public TeamMember addMemberToTeam(Member member, Team team, MemberRole role) {
    TeamMember teamMember = TeamMember.builder()
        .team(team)
        .member(member)
        .teamMemberRole(role)
        .build();

    return teamMemberRepository.save(teamMember);
  }

  @Override
  @Transactional
  public TeamMember addMember(TeamMemberRequestDTO.AddMemberDTO request) {
    Team team = teamRepository.findById(request.getTeamId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));

    Member currentMember = getRequester();

    boolean isAdmin = team.getTeamMemberList().stream()
        .anyMatch(teamMember ->
            teamMember.getMember().getMemberId().equals(currentMember.getMemberId()) &&
                teamMember.getTeamMemberRole() == MemberRole.ADMIN
        );

    if (!isAdmin) {
      throw new GeneralException(ErrorStatus.TEAM_ADMIN_UNAUTHORIZED);
    }

    Member member = memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

    TeamMember teamMember = TeamMember.builder()
        .team(team)
        .member(member)
        .teamMemberRole(request.getMemberRole())
        .build();

    return addMemberToTeam(member, team, request.getMemberRole());
  }

  @Override
  @Transactional
  public void removeMember(TeamMemberRequestDTO.RemoveMemberDTO request) {
    Team team = teamRepository.findById(request.getTeamId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));

    Member currentMember = getRequester();

    boolean isAdmin = team.getTeamMemberList().stream()
        .anyMatch(teamMember ->
            teamMember.getMember().getMemberId().equals(currentMember.getMemberId()) &&
                teamMember.getTeamMemberRole() == MemberRole.ADMIN
        );

    if (!isAdmin) {
      throw new GeneralException(ErrorStatus.TEAM_ADMIN_UNAUTHORIZED);
    }

    Member member = memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

    TeamMember teamMember = teamMemberRepository.findByTeamAndMember(team, member)
        .orElseThrow(() -> new IllegalArgumentException("Team member not found"));

    teamMemberRepository.delete(teamMember);
  }

  private Member getRequester() {
    String userEmail = SecurityUtil.getCurrentMemberEmail();
    return memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
  }
}