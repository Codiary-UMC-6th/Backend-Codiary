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
    public ApiResponse<Oauth2ResponseDTO.kakaoLoginDTO> kakaoLogin() {
        String url = socialLoginService.getRedirectUrl();
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, new Oauth2ResponseDTO.kakaoLoginDTO(url));
    }

    @GetMapping("/login/kakao")
    @Operation(summary = "카카오 서버에서 요청하는 api")
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> kekaoToken(@RequestParam String code) {
        return socialLoginService.kakaoLogin(code);
    }
}
