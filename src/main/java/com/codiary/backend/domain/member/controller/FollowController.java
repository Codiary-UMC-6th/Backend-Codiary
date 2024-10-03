package com.codiary.backend.domain.member.controller;

import com.codiary.backend.domain.member.converter.MemberConverter;
import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.codiary.backend.domain.member.entity.Follow;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.service.FollowService;
import com.codiary.backend.domain.member.service.MemberCommandService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/follow")
@Tag(name = "팔로우 API", description = "팔로우 관련 API입니다.")
public class FollowController {

    private final MemberCommandService memberCommandService;
    private final FollowService followService;

    @Operation(summary = "팔로우 및 취소 기능", description = "id를 가진 유저에 대해 팔로우하거나 취소할 수 있습니다.")
    @PostMapping("/{member_id}")
    public ApiResponse<MemberResponseDTO.FollowDTO> follow(@PathVariable("member_id")Long toId) {
        Member member = memberCommandService.getRequester();

        Follow follow = followService.follow(toId, member);

        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toFollowDto(follow));
    }

    @Operation(summary = "팔로우 여부 조회", description = "id를 가진 유저에 대해 팔로우 여부를 조회합니다.")
    @GetMapping("/{member_id}")
    public ApiResponse<Boolean> isFollowing(@PathVariable("member_id")Long toId) {
        Member member = memberCommandService.getRequester();

        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, followService.isFollowing(toId, member));
    }

    @Operation(summary = "유저가 팔로우한 팔로잉 목록 조회", description = "로그인한 유저가 팔로우한 팔로잉 목록 조회")
    @GetMapping("/following")
    public ApiResponse<List<MemberResponseDTO.SimpleMemberDTO>> getFollowings() {
        Member member = memberCommandService.getRequester();
        List<Member> followings = followService.getFollowings(member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toSimpleFollowResponseDto(followings));
    }

    @Operation(summary = "유저를 팔로우한 팔로워 목록 조회", description = "로그인한 유저를 팔로우한 팔로워 목록 조회")
    @GetMapping("/followers")
    public ApiResponse<List<MemberResponseDTO.SimpleMemberDTO>> getFollowers() {
        Member member = memberCommandService.getRequester();
        List<Member> followers = followService.getFollowers(member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toSimpleFollowResponseDto(followers));
    }
}
