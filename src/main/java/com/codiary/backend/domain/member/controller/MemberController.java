package com.codiary.backend.domain.member.controller;

import com.codiary.backend.domain.member.dto.request.MemberRequestDTO;
import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.service.MemberCommandService;
import com.codiary.backend.domain.member.service.MemberQueryService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/members")
@Tag(name = "회원 API", description = "회원가입/로그인/로그아웃/회원정보 조회/수정/삭제 관련 API입니다.")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @PostMapping("/sign-up")
    @Operation(
            summary = "회원가입"
    )
    public ApiResponse<String> signUp(@Valid @RequestBody MemberRequestDTO.MemberSignUpRequestDTO request) {
        return memberCommandService.signUp(request);
    }

    @GetMapping("/sign-up/check-email")
    @Operation(summary = "이메일 중복 확인")
    public ApiResponse<String> checkEmailDuplication(@RequestParam String email) {
        return memberCommandService.checkEmailDuplication(email);
    }

    @GetMapping("/sign-up/check-nickname")
    @Operation(summary = "닉네임 중복 확인")
    public ApiResponse<String> checkNicknameDuplication(@RequestParam String nickname) {
        return memberCommandService.checkNicknameDuplication(nickname);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> login(@Valid @RequestBody MemberRequestDTO.MemberLoginRequestDTO request) {
        return memberCommandService.login(request);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ApiResponse<String> logout(@RequestHeader("Authorization") String token) {
        Member member = memberCommandService.getRequester();
        String jwtToken = token.substring(7);
        String response = memberCommandService.logout(jwtToken, member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, response);
    }

    @PostMapping("/refresh")
    public ApiResponse<MemberResponseDTO.TokenRefreshResponseDTO> refresh(@Valid @RequestBody MemberRequestDTO.MemberRefreshRequestDTO request) {
        String jwtToken = request.refreshToken();
        MemberResponseDTO.TokenRefreshResponseDTO response = memberCommandService.refresh(jwtToken);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, response);
    }

    @PatchMapping(path = "/profile-image", consumes = "multipart/form-data")
    @Operation(
            summary = "프로필 사진 설정"
    )
    public ApiResponse<MemberResponseDTO.MemberImageDTO> updateProfileImage(@ModelAttribute MemberRequestDTO.MemberProfileImageRequestDTO request) {
        Member member = memberCommandService.getRequester();

        return memberCommandService.updateProfileImage(member, request);
    }

    @DeleteMapping("/profile-image")
    @Operation(summary = "프로필 사진 삭제")
    public ApiResponse<String> deleteProflieImage() {
        Member member = memberCommandService.getRequester();
        return memberCommandService.deleteProfileImage(member);
    }

    @GetMapping("/{memberId}/profile-image")
    @Operation(summary = "사용자 프로필 사진 조회")
    public ApiResponse<MemberResponseDTO.MemberImageDTO> getProfileImage(@PathVariable Long memberId) {
        return memberQueryService.getProfileImage(memberId);
    }
}
