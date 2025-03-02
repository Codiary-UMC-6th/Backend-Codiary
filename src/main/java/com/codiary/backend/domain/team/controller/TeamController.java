package com.codiary.backend.domain.team.controller;

import com.codiary.backend.domain.alert.service.AlertService;
import com.codiary.backend.domain.member.converter.MemberConverter;
import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.security.CustomMemberDetails;
import com.codiary.backend.domain.team.converter.TeamConverter;
import com.codiary.backend.domain.team.dto.request.TeamRequestDTO;
import com.codiary.backend.domain.team.dto.response.TeamResponseDTO;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.entity.TeamBannerImage;
import com.codiary.backend.domain.team.entity.TeamMember;
import com.codiary.backend.domain.team.entity.TeamProfileImage;
import com.codiary.backend.domain.team.enumerate.TeamMemberRole;
import com.codiary.backend.domain.team.service.TeamMemberService;
import com.codiary.backend.domain.team.service.TeamService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/team")
@RequiredArgsConstructor
@Tag(name = "팀 API", description = "팀 생성/조회/수정 관련 API 입니다.")
public class TeamController {
    private final TeamService teamService;
    private final TeamMemberService teamMemberService;
    private final AlertService alertService;

    @PostMapping("")
    @Operation(summary = "팀 생성")
    public ApiResponse<TeamResponseDTO.TeamDTO> createTeam(
            @RequestBody TeamRequestDTO.CreateTeamDTO request,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Team newTeam = teamService.createTeam(request, memberDetails.getId());
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamResponseDto(newTeam));
    }

    @GetMapping("/profile/{team_id}")
    @Operation(summary = "팀 프로필 조회")
    public ApiResponse<TeamResponseDTO.TeamProfileDTO> getTeamProfile(
            @PathVariable("team_id") Long teamId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Team fetchedTeam = teamService.getTeamProfile(teamId, memberDetails.getId());
        TeamMember currentMember = teamMemberService.getTeamMemberRoleInTeam(teamId, memberDetails.getId());
        boolean isAdmin = currentMember != null && currentMember.getTeamMemberRole() == TeamMemberRole.ADMIN;

        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamProfileResponseDto(fetchedTeam, currentMember, isAdmin));
    }

    @GetMapping("/{team_id}")
    @Operation(summary = "팀 정보 조회")
    public ApiResponse<TeamResponseDTO.TeamDTO> getTeam(
            @PathVariable("team_id") Long teamId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Team fetchedTeam = teamService.getTeam(teamId, memberDetails.getId());
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamResponseDto(fetchedTeam));
    }

    @PutMapping("/{team_id}")
    @Operation(summary = "팀 정보 수정")
    public ApiResponse<TeamResponseDTO.TeamDTO> updateTeam(
            @PathVariable("team_id") Long teamId,
            @RequestBody TeamRequestDTO.UpdateTeamDTO request,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Team updatedTeam = teamService.updateTeam(request, teamId, memberDetails.getId());
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamResponseDto(updatedTeam));
    }

    @PostMapping("/team_member")
    @Operation(summary = "팀원 추가")
    public ApiResponse<TeamResponseDTO.TeamMemberDTO> addTeamMember(
            @RequestParam("team_id") Long teamId,
            @RequestBody TeamRequestDTO.TeamMemberDTO request,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        TeamMember teamMember = teamMemberService.addTeamMember(memberDetails.getId(), teamId, request);
        alertService.sendTeamAppendAlert(teamMember);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamMemberResponseDTO(teamMember));
    }

    @DeleteMapping("/team_member")
    @Operation(summary = "팀원 삭제")
    public ApiResponse<String> deleteTeamMember(
            @RequestParam("team_id") Long teamId,
            @RequestParam("member_id") Long memberId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        teamMemberService.deleteTeamMember(memberDetails.getId(), teamId, memberId);
        alertService.sendTeamExiledAlert(teamId, memberId);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, "팀원 삭제가 완료되었습니다.");
    }

    @GetMapping("/team_member")
    @Operation(summary = "팀원 조회")
    public ApiResponse<List<TeamResponseDTO.TeamMemberDTO>> getTeamMember(
            @RequestParam("team_id") Long teamId
    ) {
        Team team = teamMemberService.getTeamMember(teamId);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamMemberListResponseDTO(team));
    }

    @PostMapping(path = "/{team_id}/profile_image", consumes = "multipart/form-data")
    @Operation(summary = "팀 프로필 사진 설정")
    public ApiResponse<TeamResponseDTO.TeamImageDTO> setTeamProfileImage(
            @RequestParam("team_id") Long teamId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @ModelAttribute TeamRequestDTO.TeamImageDTO request
    ) {
        Long memberId = memberDetails.getId();
        TeamProfileImage profileImage = teamService.setTeamProfileImage(teamId, memberId, request);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamImageResponseDTO(profileImage));
    }

    @DeleteMapping("/{team_id}/profile_image")
    @Operation(summary = "팀 프로필 사진 삭제")
    public ApiResponse<String> deleteTeamProfileImage(
            @RequestParam("team_id") Long teamId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        String response = teamService.deleteTeamProfileImage(teamId, memberId);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, response);
    }

    @PostMapping(path = "/{team_id}/banner_image", consumes = "multipart/form-data")
    @Operation(summary = "팀 배너 사진 설정")
    public ApiResponse<TeamResponseDTO.TeamImageDTO> setTeamBannerImage(
            @RequestParam("team_id") Long teamId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @ModelAttribute TeamRequestDTO.TeamImageDTO request
    ) {
        Long memberId = memberDetails.getId();
        TeamBannerImage bannerImage = teamService.setTeamBannerImage(teamId, memberId, request);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamImageResponseDTO(bannerImage));
    }

    @DeleteMapping("/{team_id}/banner_image")
    @Operation(summary = "팀 배너 사진 설정")
    public ApiResponse<String> deleteTeamBannerImage(
            @RequestParam("team_id") Long teamId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        String response = teamService.deleteTeamBannerImage(teamId, memberId);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, response);
    }


    @GetMapping("/list")
    @Operation(summary = "팀 리스트 조회 API", description = "팀 설정을 위한 팀 전체 리스트를 조회합니다.")
    public ApiResponse<TeamResponseDTO.TeamPreviewListDTO> findTeams(){
        List<Team> teams = teamService.getTeams();
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamPreviewListDTO(teams));
    }


    @GetMapping("/{member_id}/myTeam")
    @Operation(summary = "사용자의 팀 목록 조회", description = "사용자의 팀 목록 조회 기능")
    public ApiResponse<List<TeamResponseDTO.SimpleTeamDTO>> getMemberTeam(@PathVariable("member_id") Long memberId, @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        List<TeamMember> teams = teamMemberService.getMemberTeam(memberId, memberDetails.getId());
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, TeamConverter.toSimpleTeamListResponseDTO(teams));
    }


    @GetMapping("/check_duplicate")
    @Operation(summary = "팀 이름 중복 확인 API", description = "팀 이름 중복을 확인합니다.")
    public ApiResponse<String> checkDuplicateTeamName(@RequestParam("team_name") String teamName){
        teamService.checkDuplicateTeamName(teamName);
        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, null);
    }


}
