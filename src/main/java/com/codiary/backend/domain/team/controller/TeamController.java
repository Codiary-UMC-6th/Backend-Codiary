package com.codiary.backend.domain.team.controller;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.service.MemberCommandService;
import com.codiary.backend.domain.team.converter.TeamConverter;
import com.codiary.backend.domain.team.dto.request.TeamRequestDTO;
import com.codiary.backend.domain.team.dto.response.TeamResponseDTO;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.service.TeamService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@Tag(name = "팀 API", description = "팀 생성/조회/수정 관련 API 입니다.")
public class TeamController {
    private final TeamService teamService;
    private final MemberCommandService memberCommandService;

    @PostMapping("")
    @Operation(summary = "팀 생성")
    public ApiResponse<TeamResponseDTO.TeamDTO> createTeam(@RequestBody TeamRequestDTO.CreateTeamDTO request){
        Member member = memberCommandService.getRequester();
        Team newTeam = teamService.createTeam(request, member);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamResponseDto(newTeam));
    }

    @GetMapping("/profile/{team_id}")
    @Operation(summary = "팀 프로필 조회")
    public ApiResponse<TeamResponseDTO.TeamProfileDTO> getTeamProfile(@PathVariable("team_id") Long teamId){
        Member member = memberCommandService.getRequester();
        Team fetchedTeam = teamService.getTeamProfile(teamId, member);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamProfileResponseDto(fetchedTeam));
    }

    @GetMapping("/{team_id}")
    @Operation(summary = "팀 정보 조회")
    public ApiResponse<TeamResponseDTO.TeamDTO> getTeam(@PathVariable("team_id") Long teamId){
        Member member = memberCommandService.getRequester();
        Team fetchedTeam = teamService.getTeam(teamId, member);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamResponseDto(fetchedTeam));
    }

    @PutMapping("/{team_id}")
    @Operation(summary = "팀 정보 수정")
    public ApiResponse<TeamResponseDTO.TeamDTO> updateTeam(@PathVariable("team_id") Long teamId, @RequestBody TeamRequestDTO.UpdateTeamDTO request){
        Member member = memberCommandService.getRequester();
        Team updatedTeam = teamService.updateTeam(request, teamId, member);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamResponseDto(updatedTeam));
    }

}
