package com.codiary.backend.domain.member.controller;

import com.codiary.backend.domain.member.dto.request.MemberRequestDTO;
import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.codiary.backend.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v2/auth")
@Tag(name = "auth 관련 API", description = "회원가입/로그인/로그아웃 관련 API입니다.")
public class AuthController {

    @PostMapping("/sign_up")
    @Operation(summary = "회원가입")
    public ApiResponse<String> signUp(@Valid @RequestBody MemberRequestDTO.MemberSignUpRequestDTO request) {
        return null;
    }

    @GetMapping("sign_up/emails/check")
    @Operation(summary = "이메일 중복 확인")
    public ApiResponse<String> checkEmailDuplication(@Valid @RequestBody MemberRequestDTO.emailDuplicationCheckRequestDTO request) {
        return null;
    }

    @GetMapping("sign_up/nicknames/check")
    @Operation(summary = "닉네임 중복 확인")
    public ApiResponse<String> checkNicknameDuplication(@Valid @RequestBody MemberRequestDTO.nickNameDuplicationCheckRequestDTO request) {
        return null;
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> login(@Valid @RequestBody MemberRequestDTO.MemberLoginRequestDTO request) {
        return null;
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ApiResponse<String> logout(@Valid @RequestBody MemberRequestDTO.refreshRequestDTO request) {
        return null;
    }

    @PostMapping("/refresh")
    @Operation(summary = "액세스 토큰 재할당")
    public ApiResponse<MemberResponseDTO.TokenRefreshResponseDTO> refresh(@Valid @RequestBody MemberRequestDTO.refreshRequestDTO request) {
        return null;
    }
}
