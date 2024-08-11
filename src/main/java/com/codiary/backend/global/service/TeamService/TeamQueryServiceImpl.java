package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.domain.entity.TeamFollow;
import com.codiary.backend.global.repository.TeamBannerImageRepository;
import com.codiary.backend.global.repository.TeamFollowRepository;
import com.codiary.backend.global.repository.TeamProfileImageRepository;
import com.codiary.backend.global.converter.TeamConverter;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.repository.TeamRepository;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;
import lombok.RequiredArgsConstructor;
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

    team.getTeamMemberList().size();
    return TeamConverter.toTeamCheckResponseDTO(team);
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
    return followers.stream()
        .map(TeamFollow::getMember)
        .collect(Collectors.toList());
  }
}
