package com.codiary.backend.domain.team.controller;

import com.codiary.backend.domain.team.service.TeamFollowService;
import com.codiary.backend.domain.team.service.TeamService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/follow/teams/{team-id}")
public class TeamFollowController {

    @Operation(summary = "팀 팔로우/언팔로우", description = "{team-id}에 해당하는 팀에 대한 팔로우/언팔로우 기능을 수행합니다.")
    @PostMapping()
    public ApiResponse<?> FollowTeam(@PathVariable("team-id") Long teamId) {
        return null;
    }

    @Operation(summary = "팀 팔로우 여부 조회", description = "{team-id}에 해당하는 팀에 대해 팔로우 여부를 조회합니다.")
    @GetMapping()
    public ApiResponse<?> isFollowing(@PathVariable("team-id") Long teamId) {
        return null;
    }

    @Operation(summary = "팀 팔로워 리스트 조회", description = "{team-id}에 해당하는 팀의 팔로워 리스트를 조회힙니다. 팀에 속한 사용자만 사용 가능합니다.")
    @GetMapping("followers")
    public ApiResponse<?> getFollowers(@PathVariable("team-id") Long teamId) {
        return null;
    }
}
