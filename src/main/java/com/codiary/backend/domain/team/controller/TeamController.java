package com.codiary.backend.domain.team.controller;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.security.CustomMemberDetails;
import com.codiary.backend.domain.member.service.MemberCommandService;
import com.codiary.backend.domain.team.converter.TeamConverter;
import com.codiary.backend.domain.team.dto.request.TeamRequestDTO;
import com.codiary.backend.domain.team.dto.response.TeamResponseDTO;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.entity.TeamMember;
import com.codiary.backend.domain.team.service.TeamMemberService;
import com.codiary.backend.domain.team.service.TeamService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@Tag(name = "팀 API", description = "팀 생성/조회/수정 관련 API 입니다.")
public class TeamController {
    private final TeamService teamService;
    private final TeamMemberService teamMemberService;
    private final MemberCommandService memberCommandService;

    @PostMapping("")
    @Operation(summary = "팀 생성")
    public ApiResponse<TeamResponseDTO.TeamDTO> createTeam(@RequestBody TeamRequestDTO.CreateTeamDTO request,
                                                           @AuthenticationPrincipal CustomMemberDetails memberDetails){
        Team newTeam = teamService.createTeam(request, memberDetails.getId());
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamResponseDto(newTeam));
    }

    @GetMapping("/profile/{team_id}")
    @Operation(summary = "팀 프로필 조회")
    public ApiResponse<TeamResponseDTO.TeamProfileDTO> getTeamProfile(@PathVariable("team_id") Long teamId,
                                                                      @AuthenticationPrincipal CustomMemberDetails memberDetails){
        Team fetchedTeam = teamService.getTeamProfile(teamId, memberDetails.getId());
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamProfileResponseDto(fetchedTeam));
    }

    @GetMapping("/{team_id}")
    @Operation(summary = "팀 정보 조회")
    public ApiResponse<TeamResponseDTO.TeamDTO> getTeam(@PathVariable("team_id") Long teamId,
                                                        @AuthenticationPrincipal CustomMemberDetails memberDetails){
        Team fetchedTeam = teamService.getTeam(teamId, memberDetails.getId());
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamResponseDto(fetchedTeam));
    }

    @PutMapping("/{team_id}")
    @Operation(summary = "팀 정보 수정")
    public ApiResponse<TeamResponseDTO.TeamDTO> updateTeam(@PathVariable("team_id") Long teamId,
                                                           @RequestBody TeamRequestDTO.UpdateTeamDTO request,
                                                           @AuthenticationPrincipal CustomMemberDetails memberDetails){
        Team updatedTeam = teamService.updateTeam(request, teamId, memberDetails.getId());
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamResponseDto(updatedTeam));
    }

    @PostMapping("/team_member")
    @Operation(summary = "팀원 추가")
    public ApiResponse<TeamResponseDTO.TeamMemberDTO> addTeamMember(
            @RequestParam("team_id") Long teamId,
            @RequestBody TeamRequestDTO.TeamMemberDTO request,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ){
       TeamMember teamMember = teamMemberService.addTeamMember(memberDetails.getId(), teamId, request);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamMemberResponseDTO(teamMember));
    }

    @DeleteMapping("/team_member")
    @Operation(summary = "팀원 삭제")
    public ApiResponse<String> deleteTeamMember(
            @RequestParam("team_id") Long teamId,
            @RequestParam("member_id") Long memberId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ){
        teamMemberService.deleteTeamMember(memberDetails.getId(), teamId, memberId);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, "팀원 삭제가 완료되었습니다.");
    }

    @GetMapping("/team_member")
    @Operation(summary = "팀원 조회")
    public ApiResponse<List<TeamResponseDTO.TeamMemberDTO>> getTeamMember(
            @RequestParam("team_id") Long teamId
    ){
        Team team = teamMemberService.getTeamMember(teamId);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamMemberListResponseDto(team));
    }
}
