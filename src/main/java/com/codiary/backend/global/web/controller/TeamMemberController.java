package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.TeamMemberConverter;
import com.codiary.backend.global.domain.entity.mapping.TeamMember;
import com.codiary.backend.global.service.TeamMemberService.TeamMemberCommandService;
import com.codiary.backend.global.web.dto.TeamMember.TeamMemberRequestDTO;
import com.codiary.backend.global.web.dto.TeamMember.TeamMemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamMemberController {

  private final TeamMemberCommandService teamMemberCommandService;

  @PostMapping("/add")
  @Operation(summary = "팀원 추가")
  public ApiResponse<TeamMemberResponseDTO.TeamMemberDTO> addMember(
      @RequestBody TeamMemberRequestDTO.AddMemberDTO request,
      @RequestParam Long adminId // 관리자의 ID를 함께 받아 검증
  ) {
    TeamMember newMember = teamMemberCommandService.addMember(request, adminId);
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamMemberConverter.toTeamMemberDTO(newMember));
  }

  @DeleteMapping("/delete")
  @Operation(summary = "팀원 삭제")
  public ApiResponse<Void> removeMember(
      @RequestBody TeamMemberRequestDTO.RemoveMemberDTO request,
      @RequestParam Long adminId // 관리자의 ID를 함께 받아 검증
  ) {
    teamMemberCommandService.removeMember(request, adminId);
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, null);
  }
}