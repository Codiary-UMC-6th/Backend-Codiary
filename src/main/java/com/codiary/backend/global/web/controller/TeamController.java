package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.PostConverter;
import com.codiary.backend.global.converter.TeamConverter;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.service.PostService.PostCommandService;
import com.codiary.backend.global.service.TeamService.TeamCommandService;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;
import com.codiary.backend.global.web.dto.Post.PostResponseDTO;
import com.codiary.backend.global.web.dto.Team.TeamRequestDTO;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController {
  private final TeamCommandService teamCommandService;
  //팀 생성
  @PostMapping()
  @Operation(
      summary = "팀 생성"
  )
  public ApiResponse<TeamResponseDTO.CreateTeamResponseDTO> createTeam(
      @RequestBody TeamRequestDTO.CreateTeamRequestDTO request
      ){
    Team newTeam = teamCommandService.createTeam(request);
    return ApiResponse.onSuccess(
        SuccessStatus.TEAM_OK,
        TeamConverter.toCreateMemberDTO(newTeam)
    );
  }

  //팀 조회
  @GetMapping("/{teamId}")
  @Operation(
      summary = "팀 조회"
  )
  public ApiResponse<TeamResponseDTO.TeamCheckResponseDTO> checkTeam(
      @RequestParam Long teamId
  ){
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamCheckDTO());
  }

  @PatchMapping("/profile/{teamId}")
  @Operation(
      summary = "팀 프로필 수정"
  )
  public ApiResponse<TeamResponseDTO.UpdateTeamDTO> updateTeam(
      @RequestBody TeamRequestDTO.UpdateTeamDTO request,
      @PathVariable Long teamId
  ){
    return ApiResponse.onSuccess(
        SuccessStatus.TEAM_OK,
        TeamConverter.toUpdateTeamDTO(
            teamCommandService.updateTeam(teamId)
        )
    );
  }
}
