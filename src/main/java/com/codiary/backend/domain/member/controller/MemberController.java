package com.codiary.backend.domain.member.controller;

import com.codiary.backend.domain.member.converter.MemberConverter;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.service.MemberCommandService;
import com.codiary.backend.domain.member.service.MemberQueryService;
import com.codiary.backend.domain.techstack.enumerate.TechStack;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.domain.member.dto.request.MemberRequestDTO;
import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
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
    @Operation(
            summary = "로그인"
    )
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

    @GetMapping("/profile/{member_id}")
    @Operation(summary = "사용자 프로필 기본 정보 조회", description = "마이페이지 사용자 정보 조회 기능")
    public ApiResponse<MemberResponseDTO.SimpleMemberDTO> getUserProfile(@PathVariable(value = "member_id") Long memberId){
        Member member = memberCommandService.getRequester();
        Member user = memberQueryService.getUserProfile(memberId);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toSimpleMemberResponseDto(member, user));
    }

    @PutMapping("/info")
    @Operation(summary = "사용자 정보 수정", description = "마이페이지 사용자 정보 수정 기능")
    public ApiResponse<MemberResponseDTO.MemberInfoDTO> updateUserInfo(@Valid @RequestBody MemberRequestDTO.MemberInfoDTO request){
        Member member = memberCommandService.getRequester();
        Member updatedMember = memberCommandService.updateMemberInfo(member, request);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toMemberInfoResponseDto(updatedMember));
    }

    @GetMapping("/info")
    @Operation(summary = "사용자 정보 조회", description = "마이페이지 사용자 정보 조회 기능")
    public ApiResponse<MemberResponseDTO.MemberInfoDTO> getUserInfo(){
        Member member = memberCommandService.getRequester();
        Member fetchedMember = memberQueryService.getUserInfo(member.getMemberId());
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toMemberInfoResponseDto(fetchedMember));
    }

    @PatchMapping("/techstack/{techstack_name}")
    @Operation(summary = "사용자 기술스택 추가", description = "마이페이지 사용자 기술스택 추가 기능")
    public ApiResponse<MemberResponseDTO.MemberTechStackDTO> addTechStack(@PathVariable(value = "techstack_name") TechStack techStackName){
        Member member = memberCommandService.getRequester();
        Member updatedMember = memberCommandService.addTechStack(member.getMemberId(),techStackName);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toMemberTechStackResponseDto(updatedMember));
    }
}
