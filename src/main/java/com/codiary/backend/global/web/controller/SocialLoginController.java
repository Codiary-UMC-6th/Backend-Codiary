package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.service.MemberService.SocialLoginService;
import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;
import com.codiary.backend.global.web.dto.SocialLogin.Oauth2ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "소셜 로그인", description = "소셜 로그인 관련 API입니다.")
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    @PostMapping("/login/kakao")
    @Operation(summary = "카카오 로그인")
    public ApiResponse<Oauth2ResponseDTO.SocialLoginDTO> kakaoLogin() {
        String url = socialLoginService.getKakaoRedirectUrl();
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, new Oauth2ResponseDTO.SocialLoginDTO(url));
    }

    @GetMapping("/login/kakao")
    @Operation(summary = "카카오 서버에서 요청하는 api")
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> kakaoToken(@RequestParam String code) {
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, socialLoginService.kakaoLogin(code));
    }

    @PostMapping("/login/naver")
    @Operation(summary = "네이버 로그인")
    public ApiResponse<Oauth2ResponseDTO.SocialLoginDTO> naverLogin() {
        String url = socialLoginService.getNaverRedirectUrl();
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, new Oauth2ResponseDTO.SocialLoginDTO(url));
    }

    @GetMapping("/login/naver")
    @Operation(summary = "네이버에서 요청하는 api")
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> naverToken(
            @RequestParam String code,
            @RequestParam String state
    ) {
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, socialLoginService.naverLogin(code, state));
    }

    @PostMapping("/login/github")
    @Operation(summary = "깃허브 로그인")
    public ApiResponse<Oauth2ResponseDTO.SocialLoginDTO> githubLogin() {
        String url = socialLoginService.getGithubRedirectUrl();
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, new Oauth2ResponseDTO.SocialLoginDTO(url));
    }

    @GetMapping("/login/github")
    @Operation(summary = "깃허브에서 요청하는 api")
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> githubToken(
            @RequestParam String code
    ) {
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, socialLoginService.githubLogin(code));
    }

    @PostMapping("/login/google")
    @Operation(summary = "구글 로그인")
    public ApiResponse<Oauth2ResponseDTO.SocialLoginDTO> googleLogin() {
        String url = socialLoginService.getGoogleRedirectUrl();
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, new Oauth2ResponseDTO.SocialLoginDTO(url));
    }

    @GetMapping("/login/google")
    @Operation(summary = "구글에서 요청하는 api")
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> googleToken(@RequestParam String code) {
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, socialLoginService.googleLogin(code));
    }
}
