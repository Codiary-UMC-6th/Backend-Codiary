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
  @PostMapping(consumes = "multipart/form-data")
  @Operation(summary = "팀 생성")
  public ApiResponse<TeamResponseDTO.CreateTeamResponseDTO> createTeam(
          @ModelAttribute TeamRequestDTO.CreateTeamRequestDTO request
      ){
    Team newTeam = teamCommandService.createTeam(request);
    return ApiResponse.onSuccess(
        SuccessStatus.TEAM_OK,
        TeamConverter.toCreateMemberDTO(newTeam));
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


}
