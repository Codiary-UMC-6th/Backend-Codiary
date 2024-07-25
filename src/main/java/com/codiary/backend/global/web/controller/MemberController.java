package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.service.MemberService.MemberCommandServiceImpl;
import com.codiary.backend.global.web.dto.Member.MemberRequestDTO;
import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.service.MemberService.FollowService;
import com.codiary.backend.global.web.dto.Member.FollowResponseDto;
import com.codiary.backend.global.web.dto.Member.MemberSumResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberCommandServiceImpl memberCommandService;
    private final FollowService followService;

    @PostMapping("/sign-up")
    @Operation(
            summary = "회원가입"
    )
    public ApiResponse<String> signUp(@Valid @RequestBody MemberRequestDTO.MemberSignUpRequestDTO request) {
        return memberCommandService.signUp(request);
    }

    @PostMapping("/login")
    @Operation(
            summary = "로그인"
    )
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> login(@Valid @RequestBody MemberRequestDTO.MemberLoginRequestDTO request) {
        return memberCommandService.login(request);
    }

    
    //TODO: 로그인 구현 완료 시 principal 추가(follow 주체) 필요
    @PostMapping("/follow/{id}")
    public ApiResponse<FollowResponseDto> follow(@PathVariable("id") Long toId) {
        Long fromId = 1L;
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, followService.follow(toId, fromId));
    }

    //TODO: 로그인 구현 완료 시 principal 추가(follow 주체) 필요
    @GetMapping("/follow/{id}")
    public ApiResponse<Boolean> isFollowing(@PathVariable("id") Long toId) {
        Long fromId = 1L;
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, followService.isFollowing(toId, fromId));
    }

    //TODO: 로그인 구현 완료 시 principal 추가(follow 주체) 필요
    @GetMapping("/following")
    public ApiResponse<List<MemberSumResponseDto>> getFollowings() {
        Long id = 1L;
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, followService.getFollowings(id));
    }

    //TODO: 로그인 구현 완료 시 principal 추가(follow 주체) 필요
    @GetMapping("/follower")
    public ApiResponse<List<MemberSumResponseDto>> getFollowers() {
        Long id = 3L;
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, followService.getFollowers(id));
    }
}
