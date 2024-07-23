package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.service.MemberService.FollowService;
import com.codiary.backend.global.web.dto.Member.FollowResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final FollowService followService;

    //TODO: 로그인 구현 완료 시 principal 추가(follow 주체) 필요
    @PostMapping("/follow/{id}")
    public ApiResponse<FollowResponseDto> follow(@PathVariable("id") Long toId) {
        Long fromId = 1L;
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, followService.follow(toId, fromId));
    }



}
