package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;

public interface TeamQueryService {
  TeamResponseDTO.TeamCheckResponseDTO getTeamById(Long teamId);
}
