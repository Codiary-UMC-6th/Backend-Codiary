package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.service.MemberService.SocialLoginService;
import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;
import com.codiary.backend.global.web.dto.SocialLogin.Oauth2ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    @PostMapping("/login/kakao")
    @Operation(summary = "카카오 로그인")
    public ApiResponse<Oauth2ResponseDTO.KakaoLoginDTO> kakaoLogin() {
        String url = socialLoginService.getKakaoRedirectUrl();
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, new Oauth2ResponseDTO.KakaoLoginDTO(url));
    }

    @GetMapping("/login/kakao")
    @Operation(summary = "카카오 서버에서 요청하는 api")
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> kakaoToken(@RequestParam String code) {
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, socialLoginService.kakaoLogin(code));
    }

    @PostMapping("/login/naver")
    @Operation(summary = "네이버 로그인")
    public ApiResponse<Oauth2ResponseDTO.NaverLoginDTO> naverLogin() {
        String url = socialLoginService.getNaverRedirectUrl();
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, new Oauth2ResponseDTO.NaverLoginDTO(url));
    }

    @GetMapping("/login/naver")
    @Operation(summary = "네이버에서 요청하는 api")
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> naverToken(
            @RequestParam String code,
            @RequestParam String state
    ) {
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, socialLoginService.naverLogin(code, state));
    }
}
