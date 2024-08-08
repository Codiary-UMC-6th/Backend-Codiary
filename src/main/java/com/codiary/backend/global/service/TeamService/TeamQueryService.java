package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;
import org.springframework.stereotype.Service;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;

@Service
public interface TeamQueryService {

    ApiResponse<TeamResponseDTO.TeamImageDTO> getBannerImage(Long teamId);

    ApiResponse<TeamResponseDTO.TeamImageDTO> getProfileImage(Long teamId);

    TeamResponseDTO.TeamCheckResponseDTO getTeamById(Long teamId);
}
