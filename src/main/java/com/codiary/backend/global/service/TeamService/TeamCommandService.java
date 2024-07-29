package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.web.dto.Team.TeamRequestDTO;

public interface TeamCommandService {
  //팀 생성
  Team createTeam(TeamRequestDTO.CreateTeamRequestDTO request);
}
