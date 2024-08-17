package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.TeamMemberConverter;
import com.codiary.backend.global.domain.entity.mapping.TeamMember;
import com.codiary.backend.global.service.TeamMemberService.TeamMemberCommandService;
import com.codiary.backend.global.web.dto.TeamMember.TeamMemberRequestDTO;
import com.codiary.backend.global.web.dto.TeamMember.TeamMemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
@Tag(name = "팀원 API", description = "팀원 추가/삭제 관련 API입니다.")
public class TeamMemberController {

  private final TeamMemberCommandService teamMemberCommandService;

  @PostMapping("/add")
  @Operation(summary = "팀원 추가")
  public ApiResponse<TeamMemberResponseDTO.TeamMemberDTO> addMember(@RequestBody TeamMemberRequestDTO.AddMemberDTO request) {
    TeamMember newMember = teamMemberCommandService.addMember(request);
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamMemberConverter.toTeamMemberDTO(newMember));
  }

  @DeleteMapping("/delete")
  @Operation(summary = "팀원 삭제")
  public ApiResponse<Void> removeMember(@RequestBody TeamMemberRequestDTO.RemoveMemberDTO request) {
    teamMemberCommandService.removeMember(request);
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, null);
  }
}