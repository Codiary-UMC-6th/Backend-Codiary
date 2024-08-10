package com.codiary.backend.global.service.TeamMemberService;

import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.domain.entity.mapping.TeamMember;
import com.codiary.backend.global.domain.enums.MemberRole;
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

  @Override
  @Transactional
  public TeamMember addMember(TeamMemberRequestDTO.AddMemberDTO request, Long adminId) {
    // 팀과 멤버 엔티티를 조회
    Team team = teamRepository.findById(request.getTeamId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));

    Member member = memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

    // 관리자 권한 검증
    validateAdminRole(team, adminId);

    // 새로운 팀 멤버 생성 및 저장
    TeamMember teamMember = TeamMember.builder()
        .team(team)
        .member(member)
        .teamMemberRole(request.getMemberRole())  // 관리자 또는 일반 멤버 역할 설정
        .build();

    return teamMemberRepository.save(teamMember);
  }

  @Override
  @Transactional
  public void removeMember(TeamMemberRequestDTO.RemoveMemberDTO request, Long adminId) {
    // 팀과 멤버 엔티티를 조회
    Team team = teamRepository.findById(request.getTeamId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));

    Member member = memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

    // 관리자 권한 검증
    validateAdminRole(team, adminId);

    // 팀 멤버 엔티티를 조회하고 삭제
    TeamMember teamMember = teamMemberRepository.findByTeamAndMember(team, member)
        .orElseThrow(() -> new IllegalArgumentException("Team member not found"));

    teamMemberRepository.delete(teamMember);
  }

  // 관리자 권한을 확인하는 메서드
  private void validateAdminRole(Team team, Long adminId) {
    TeamMember adminMember = teamMemberRepository.findByTeam_TeamIdAndMember_MemberId(team.getTeamId(), adminId)
        .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

    if (adminMember.getTeamMemberRole() != MemberRole.ADMIN) {
      throw new IllegalStateException("Only admins can perform this action");
    }
  }
}