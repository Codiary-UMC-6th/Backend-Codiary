package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.PostConverter;
import com.codiary.backend.global.converter.TeamConverter;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.service.PostService.PostCommandService;
import com.codiary.backend.global.service.TeamService.TeamCommandService;
import com.codiary.backend.global.service.TeamService.TeamQueryService;
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
  @GetMapping("/{teamId}/members/{memberId}")
  @Operation(summary = "팀 정보 조회")
  public ApiResponse<TeamResponseDTO.TeamCheckResponseDTO> getTeamById(
      @PathVariable Long teamId,
      @PathVariable Long memberId) {
    TeamResponseDTO.TeamCheckResponseDTO teamInfo = teamQueryService.getTeamById(teamId, memberId);
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, teamInfo);
  }

  // 팀 프로필 수정
  @PatchMapping("/profile/{teamId}")
  @Operation(summary = "팀 프로필 수정")
  public ApiResponse<Void> updateTeamProfile(
      @PathVariable Long teamId,
      @RequestBody TeamRequestDTO.UpdateTeamDTO request,
      @RequestParam Long memberId) {

    TeamResponseDTO.TeamCheckResponseDTO teamInfo = teamQueryService.getTeamById(teamId, memberId);

    if (!teamInfo.isAdmin()) {
      return ApiResponse.onFailure("UNAUTHORIZED", "권한이 없습니다.", null);
    }

    teamCommandService.updateTeam(teamId, request);
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, null);
  }

  //팀 프로젝트 생성
  @PostMapping("/{teamId}/projects")
  @Operation(summary = "팀 프로젝트 생성")
  public ApiResponse<TeamResponseDTO.ProjectDTO> createProject(
      @PathVariable Long teamId,
      @RequestParam Long memberId,
      @RequestBody TeamRequestDTO.CreateProjectDTO request) {
    TeamResponseDTO.ProjectDTO project = teamCommandService.createProject(teamId, memberId, request);
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, project);
  }
  //팀 팔로우


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

}
