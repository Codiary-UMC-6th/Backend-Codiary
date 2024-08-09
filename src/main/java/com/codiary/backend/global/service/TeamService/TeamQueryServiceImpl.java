package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.repository.TeamBannerImageRepository;
import com.codiary.backend.global.repository.TeamProfileImageRepository;
import com.codiary.backend.global.converter.TeamConverter;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.repository.TeamRepository;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TeamQueryServiceImpl implements TeamQueryService {

    private final TeamRepository teamRepository;

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
    public TeamResponseDTO.TeamCheckResponseDTO getTeamById(Long teamId) {
      Team team = teamRepository.findById(teamId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));

      return TeamConverter.toTeamCheckResponseDTO(team);
  }
}
