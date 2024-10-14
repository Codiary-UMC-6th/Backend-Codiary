package com.codiary.backend.domain.team.controller;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.service.MemberCommandService;
import com.codiary.backend.domain.team.converter.TeamConverter;
import com.codiary.backend.domain.team.dto.response.TeamResponseDTO;
import com.codiary.backend.domain.team.entity.TeamFollow;
import com.codiary.backend.domain.team.service.TeamFollowService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/follow/teams/{team_id}")
@Tag(name = "팀 팔로우 API", description = "팀 팔로우와 관련된 API 입니다.")
public class TeamFollowController {

    private final TeamFollowService teamFollowService;
    private final MemberCommandService memberCommandService;

    @Operation(summary = "팀 팔로우/언팔로우", description = "{team_id}에 해당하는 팀에 대한 팔로우/언팔로우 기능을 수행합니다.")
    @PostMapping()
    public ApiResponse<TeamResponseDTO.TeamFollowDTO> followTeam(@PathVariable("team_id") Long teamId) {
        Member requester = memberCommandService.getRequester();
        TeamFollow teamFollow = teamFollowService.followTeam(teamId, requester);

        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamFollowResponseDTO(teamFollow));
    }

    @Operation(summary = "팀 팔로우 여부 조회", description = "{team_id}에 해당하는 팀에 대해 팔로우 여부를 조회합니다.")
    @GetMapping()
    public ApiResponse<Boolean> isFollowing(@PathVariable("team_id") Long teamId) {
        Member member = memberCommandService.getRequester();
        Boolean followStatus = teamFollowService.isFollowing(teamId, member);

        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, followStatus);
    }

    @Operation(summary = "팀 팔로워 리스트 조회", description = "{team_id}에 해당하는 팀의 팔로워 리스트를 조회힙니다. 팀에 소속된 사용자만 사용 가능합니다.")
    @GetMapping("followers")
    public ApiResponse<TeamResponseDTO.TeamFollowersDTO> getFollowers(@PathVariable("team_id") Long teamId) {
        Member member = memberCommandService.getRequester();
        List<TeamFollow> followers = teamFollowService.getFollowers(teamId, member);

        return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, TeamConverter.toTeamFollowersResponseDTO(teamId, followers));
    }
}
