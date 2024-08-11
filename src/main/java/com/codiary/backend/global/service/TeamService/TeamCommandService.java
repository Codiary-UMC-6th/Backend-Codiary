package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.domain.entity.TeamFollow;
import com.codiary.backend.global.web.dto.Team.TeamRequestDTO;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;
import com.codiary.backend.global.web.dto.TeamMember.TeamMemberRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface TeamCommandService {
  //팀 생성
  Team createTeam(TeamRequestDTO.CreateTeamRequestDTO request);

  // 팀 프로필 수정
  Team updateTeam(Long teamId, TeamRequestDTO.UpdateTeamDTO request);

  TeamFollow followTeam(Long teamId, Member fromMember);

  Member getRequester();

  TeamResponseDTO.ProjectsDTO createTeamProject(Long teamId, String projectName);

  ApiResponse<TeamResponseDTO.TeamImageDTO> updateTeamBannerImage(Long teamId, TeamRequestDTO.TeamImageRequestDTO request);

  ApiResponse<TeamResponseDTO.TeamImageDTO> updateTeamProfileImage(Long teamId, TeamRequestDTO.TeamImageRequestDTO request);

  ApiResponse<String> deleteTeamBannerImage(Long teamId);

  ApiResponse<String> deleteTeamProfileImage(Long teamId);
}
