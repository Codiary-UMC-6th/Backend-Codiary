package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.TeamConverter;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.service.PostService.PostCommandService;
import com.codiary.backend.global.service.TeamService.TeamCommandService;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController {
  private final TeamCommandService teamCommandService;
  @PostMapping()
  @Operation(
      summary = "팀 생성"
  )
  public ApiResponse<TeamResponseDTO.CreateTeamResponseDTO> createTeam(
      @RequestBody TeamResponseDTO.CreateTeamResponseDTO request
      ){
    Long teamId = 1L;
    Long projectId = 1L;
    Team newTeam = teamCommandService.createTeam(teamId, projectId, request);
    return ApiResponse.onSuccess(
        SuccessStatus.TEAM_OK,
        TeamConverter.toCreateMemberDTO(newTeam)
    )
  }

}
