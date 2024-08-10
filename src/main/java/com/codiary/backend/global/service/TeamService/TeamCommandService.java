package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.web.dto.Team.TeamRequestDTO;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;
import com.codiary.backend.global.web.dto.TeamMember.TeamMemberRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface TeamCommandService {
  //팀 생성
  Team createTeam(TeamRequestDTO.CreateTeamRequestDTO request);

  //팀 프로필 수정
  Team updateTeam(Long teamId, TeamRequestDTO.UpdateTeamDTO request);

  //팀 프로젝트 생성
  TeamResponseDTO.ProjectDTO createProject(Long teamId, Long memberId, TeamRequestDTO.CreateProjectDTO request);

  ApiResponse<TeamResponseDTO.TeamImageDTO> updateTeamBannerImage(Long teamId, TeamRequestDTO.TeamImageRequestDTO request);

  ApiResponse<TeamResponseDTO.TeamImageDTO> updateTeamProfileImage(Long teamId, TeamRequestDTO.TeamImageRequestDTO request);

  ApiResponse<String> deleteTeamBannerImage(Long teamId);

  ApiResponse<String> deleteTeamProfileImage(Long teamId);
}
