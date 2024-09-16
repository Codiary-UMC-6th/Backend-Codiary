package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.TeamConverter;
import com.codiary.backend.global.domain.entity.*;
import com.codiary.backend.global.service.TeamService.TeamCommandService;
import com.codiary.backend.global.service.TeamService.TeamQueryService;
import com.codiary.backend.global.web.dto.Team.TeamRequestDTO;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;
import com.codiary.backend.global.web.dto.Team.TeamSumResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
@CrossOrigin(origins = "http://localhost:8080, http://localhost:3000")
@Tag(name = "팀 API", description = "팀 생성/조회/수정 관련 API 입니다.")
public class TeamController {

  private final TeamCommandService teamCommandService;
  private final TeamQueryService teamQueryService;

  //팀 생성
  @PostMapping()
  @Operation(summary = "팀 생성")
  public ApiResponse<TeamResponseDTO.CreateTeamResponseDTO> createTeam(
      @RequestBody TeamRequestDTO.CreateTeamRequestDTO request
  ){
    Team newTeam = teamCommandService.createTeam(request);
    return ApiResponse.onSuccess(
        SuccessStatus.TEAM_OK,
        TeamConverter.toCreateMemberDTO(newTeam));
  }

  //팀 조회
  @GetMapping("/{teamId}")
  @Operation(summary = "팀 정보 조회")
  public ApiResponse<TeamResponseDTO.TeamCheckResponseDTO> getTeamById(@PathVariable Long teamId) {
    TeamResponseDTO.TeamCheckResponseDTO teamInfo = teamQueryService.getTeamById(teamId);
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, teamInfo);
  }

  // 팀 프로필 수정
  @PatchMapping("/profile/{teamId}")
  @Operation(summary = "팀 프로필 수정")
  public ApiResponse<TeamResponseDTO.UpdateTeamDTO> updateTeam(
      @RequestBody TeamRequestDTO.UpdateTeamDTO request,
      @PathVariable Long teamId) {
    Team updatedTeam = teamCommandService.updateTeam(teamId, request);
    return ApiResponse.onSuccess(
        SuccessStatus.TEAM_OK,
        TeamConverter.toUpdateTeamDTO(updatedTeam));
  }

  // 팀 팔로우 및 취소 기능
  @PostMapping("/follow/{teamId}")
  @Operation(summary = "팀 팔로우 및 취소 기능")
  public ApiResponse<TeamResponseDTO.TeamFollowResponseDto> followTeam(@PathVariable("teamId") Long teamId) {
    Member currentMember = teamCommandService.getRequester();
    TeamFollow teamFollow = teamCommandService.followTeam(teamId, currentMember);
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamFollowResponseDto(teamFollow));
  }

  // 팀 팔로우 여부 조회
  @GetMapping("/follow/{teamId}")
  @Operation(summary = "팀 팔로우 여부 조회 기능")
  public ApiResponse<Boolean> isFollowingTeam(@PathVariable("teamId") Long teamId) {
    Member currentMember = teamCommandService.getRequester();
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, teamQueryService.isFollowingTeam(teamId, currentMember));
  }

  //팀 팔로워 목ㅎ록 조회
  @Operation(
      summary = "팀을 팔로우한 팔로워 목록 조회",
      description = "특정 팀을 팔로우한 팔로워 목록 조회"
  )
  @GetMapping("/{teamId}/followers")
  public ApiResponse<List<TeamSumResponseDTO>> getTeamFollowers(@PathVariable Long teamId) {
    List<Member> followers = teamQueryService.getTeamFollowers(teamId);
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamFollowerResponseDto(followers));
  }

  @PostMapping("/{teamId}/project")
  @Operation(summary = "팀 프로젝트 추가하기", description = "팀에 프로젝트 하나씩 추가")
  public ApiResponse<TeamResponseDTO.ProjectsDTO> createTeamProject(
      @PathVariable Long teamId,
      @RequestBody TeamRequestDTO.CreateTeamProjectRequestDTO requestDTO) {

    return ApiResponse.onSuccess(
        SuccessStatus.TEAM_OK,
        teamCommandService.createTeamProject(teamId, requestDTO.projectName())
    );
  }

  // 팀 이미지 설정
  @PatchMapping(path = "/{teamId}/bannerImage", consumes = "multipart/form-data")
  @Operation(summary = "팀 배너 사진 설정")
  public ApiResponse<TeamResponseDTO.TeamImageDTO> updateBannerImage(
      @ModelAttribute TeamRequestDTO.TeamImageRequestDTO request,
      @PathVariable Long teamId
  ) {
    return teamCommandService.updateTeamBannerImage(teamId, request);
  }

  @PatchMapping(path = "/{teamId}/profileImage", consumes = "multipart/form-data")
  @Operation(summary = "팀 프로필 사진 설정")
  public ApiResponse<TeamResponseDTO.TeamImageDTO> updateProfileImage(
      @ModelAttribute TeamRequestDTO.TeamImageRequestDTO request,
      @PathVariable Long teamId) {
    return teamCommandService.updateTeamProfileImage(teamId, request);
  }

  // 팀 이미지 조회
  @GetMapping("/{teamId}/bannerImage")
  @Operation(summary = "팀 배너 사진 주소 요청")
  public ApiResponse<TeamResponseDTO.TeamImageDTO> getBannerImage(
      @PathVariable Long teamId
  ) {
    return teamQueryService.getBannerImage(teamId);
  }

  @GetMapping("/{teamId}/profileImage")
  @Operation(summary = "팀 프로필 사진 주소 요청")
  public ApiResponse<TeamResponseDTO.TeamImageDTO> getProfileImage(
      @PathVariable Long teamId
  ) {
    return teamQueryService.getProfileImage(teamId);
  }

  // 이미지 삭제 요청
  @DeleteMapping("/{teamId}/bannerImage")
  @Operation(summary = "팀 배너 사진 삭제 요청")
  public ApiResponse<String> deleteBannerImage(
      @PathVariable Long teamId
  ) {
    return teamCommandService.deleteTeamBannerImage(teamId);
  }

  @DeleteMapping("/{teamId}/profileImage")
  @Operation(summary = "팀 프로필 사진 삭제 요청")
  public ApiResponse<String> deleteProfileImage(
      @PathVariable Long teamId
  ) {
    return teamCommandService.deleteTeamProfileImage(teamId);
  }


  // 팀 설정을 위한 팀 리스트 조회
  @GetMapping("/list")
  @Operation(summary = "팀 리스트 조회 API", description = "팀 설정을 위한 팀 전체 리스트를 조회합니다.", security = @SecurityRequirement(name = "accessToken"))
  public ApiResponse<TeamResponseDTO.TeamPreviewListDTO> findTeams(){
    List<Team> teams = teamQueryService.getTeams();
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamPreviewListDTO(teams));
  }

}