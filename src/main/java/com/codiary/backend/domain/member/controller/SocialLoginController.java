package com.codiary.backend.domain.member.controller;

import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.codiary.backend.domain.member.dto.response.Oauth2ResponseDTO;
import com.codiary.backend.domain.member.service.SocialLoginService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v2/oauth")
@Tag(name = "Social Login API", description = "소셜 로그인 관련 API입니다.")
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    @GetMapping("/login/kakao_url")
    @Operation(summary = "카카오 로그인 url 요청")
    public ApiResponse<Oauth2ResponseDTO> kakaoLogin(@RequestParam("redirect_uri") String redirectUri) {
        String url = socialLoginService.getKakaoRedirectUrl(redirectUri);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, new Oauth2ResponseDTO(url));
    }

    @PostMapping("/login/kakao")
    @Operation(summary = "카카오로 로그인")
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> kakaoToken(
            @RequestParam("code") String code,
            @RequestParam("redirect_uri") String redirectUri
    ) {
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, socialLoginService.kakaoLogin(code, redirectUri));
    }

    @GetMapping("/login/naver_url")
    @Operation(summary = "네이버 로그인 url 요청")
    public ApiResponse<Oauth2ResponseDTO> naverLogin(@RequestParam("redirect_uri") String redirectUri) {
        String url = socialLoginService.getNaverRedirectUrl(redirectUri);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, new Oauth2ResponseDTO(url));
    }

    @PostMapping("/login/naver")
    @Operation(summary = "네이버로 로그인")
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> naverToken(
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, socialLoginService.naverLogin(code, state));
    }

    @GetMapping("/login/github_url")
    @Operation(summary = "깃허브 로그인 url 요청")
    public ApiResponse<Oauth2ResponseDTO> githubLogin(@RequestParam("redirect_uri") String redirectUri) {
        String url = socialLoginService.getGithubRedirectUrl(redirectUri);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, new Oauth2ResponseDTO(url));
    }

    @PostMapping("/login/github")
    @Operation(summary = "깃허브로 로그인")
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> githubToken(
            @RequestParam("code") String code
    ) {
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, socialLoginService.githubLogin(code));
    }

    @GetMapping("/login/google_url")
    @Operation(summary = "구글 로그인 url 요청")
    public ApiResponse<Oauth2ResponseDTO> googleLogin(@RequestParam("redirect_uri") String redirectUri) {
        String url = socialLoginService.getGoogleRedirectUrl(redirectUri);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, new Oauth2ResponseDTO(url));
    }

    @PostMapping("/login/google")
    @Operation(summary = "구글로 로그인")
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> googleToken(
            @RequestParam("code") String code,
            @RequestParam("redirect_uri") String redirectUri
    ) {
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, socialLoginService.googleLogin(code, redirectUri));
    }
}
