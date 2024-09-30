package com.codiary.backend.domain.member.controller;

import com.codiary.backend.domain.member.converter.MemberConverter;
import com.codiary.backend.domain.member.entity.Follow;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.service.FollowService;
import com.codiary.backend.domain.member.service.MemberCommandService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/members/follow")
@Tag(name = "팔로우 API", description = "팔로우 관련 API입니다.")
public class FollowController {

    private final MemberCommandService memberCommandService;
    private final FollowService followService;

    @Operation(summary = "팔로우 및 취소 기능", description = "id를 가진 유저에 대해 팔로우하거나 취소할 수 있습니다.")
    @PostMapping("/{member_id}")
    public ApiResponse<?> follow(@PathVariable("member_id")Long toId) {
        Member member = memberCommandService.getRequester();

        Follow follow = followService.follow(toId, member);

        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toFollowDto(follow));
    }

}
