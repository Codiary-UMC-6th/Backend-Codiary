package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.domain.entity.*;
import com.codiary.backend.global.repository.TeamBannerImageRepository;
import com.codiary.backend.global.repository.TeamFollowRepository;
import com.codiary.backend.global.repository.TeamProfileImageRepository;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.TeamFollow;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.domain.enums.MemberRole;
import com.codiary.backend.global.jwt.SecurityUtil;
import com.codiary.backend.global.repository.*;
import com.codiary.backend.global.converter.TeamConverter;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;
import com.codiary.backend.global.web.dto.TeamMember.TeamMemberResponseDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeamQueryServiceImpl implements TeamQueryService {

  private final TeamRepository teamRepository;
  private final TeamFollowService teamFollowService;
  private final TeamFollowRepository teamFollowRepository;
  private final MemberRepository memberRepository;

  @Override
  public ApiResponse<TeamResponseDTO.TeamImageDTO> getBannerImage(Long teamId) {
    Team team = teamRepository.findById(teamId).orElseThrow(); // 예외 처리 필요

    TeamResponseDTO.TeamImageDTO response = new TeamResponseDTO.TeamImageDTO(
        (team.getBannerImage() != null)
            ? team.getBannerImage().getImageUrl()
            : "");

    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, response);
  }

  @Override
  public ApiResponse<TeamResponseDTO.TeamImageDTO> getProfileImage(Long teamId) {
    Team team = teamRepository.findById(teamId).orElseThrow(); // 예외 처리 필요

    TeamResponseDTO.TeamImageDTO response = new TeamResponseDTO.TeamImageDTO(
        (team.getProfileImage() != null)
            ? team.getProfileImage().getImageUrl()
            : "");

    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, response);
  }

  @Override
  @Transactional(readOnly = true)
  public TeamResponseDTO.TeamCheckResponseDTO getTeamById(Long teamId) {
    Team team = teamRepository.findById(teamId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));

    Member currentMember = getRequester();

    boolean isAdmin = team.getTeamMemberList().stream()
        .anyMatch(teamMember ->
            teamMember.getMember().getMemberId().equals(currentMember.getMemberId()) &&
                teamMember.getTeamMemberRole() == MemberRole.ADMIN
        );
    team.getTeamProjectMapList().size();

    List<TeamMemberResponseDTO.TeamMemberDTO> members = team.getTeamMemberList().stream()
        .map(teamMember -> TeamMemberResponseDTO.TeamMemberDTO.builder()
            .teamMemberId(teamMember.getTeamMemberId())
            .teamId(team.getTeamId())
            .memberId(teamMember.getMember().getMemberId())
            .nickname(teamMember.getMember().getNickname())
            .memberRole(teamMember.getTeamMemberRole())
            .memberPosition(teamMember.getMemberPosition())
            .build())
        .collect(Collectors.toList());

    List<TeamResponseDTO.TeamCheckResponseDTO.ProjectDTO> projects = team.getTeamProjectMapList().stream()
        .map(teamProjectMap -> TeamResponseDTO.TeamCheckResponseDTO.ProjectDTO.builder()
            .projectId(teamProjectMap.getProject().getProjectId())
            .projectName(teamProjectMap.getProject().getProjectName())
            .build())
        .collect(Collectors.toList());

    return TeamConverter.toTeamCheckResponseDTO(team, isAdmin, members, projects);
  }

  private Member getRequester() {
    String userEmail = SecurityUtil.getCurrentMemberEmail(); // 현재 사용자의 이메일을 가져옴
    return memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
  }

  @Override
  public Boolean isFollowingTeam(Long teamId, Member fromMember) {
    return teamFollowService.isFollowingTeam(teamId, fromMember);
  }

  //팀 팔로워 목록 조회
  @Override
  @Transactional(readOnly = true)
  public List<Member> getTeamFollowers(Long teamId) {
    Team team = teamRepository.findById(teamId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

    List<TeamFollow> followers = teamFollowRepository.findByTeamAndFollowStatusTrue(team);

    // 모든 팔로워의 Member 객체를 초기화
    followers.forEach(follower -> {
      Hibernate.initialize(follower.getMember());
    });

    return followers.stream()
        .map(TeamFollow::getMember)
        .collect(Collectors.toList());
  }

  // 팀 전체 리스트 조회
  @Override
  public List<Team> getTeams() {
    return teamRepository.findAllByOrderByTeamIdDesc();
  }
}
