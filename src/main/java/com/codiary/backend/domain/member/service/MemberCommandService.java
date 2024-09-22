package com.codiary.backend.domain.member.service;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.entity.MemberCategory;
import com.codiary.backend.domain.techstack.enumerate.TechStack;
import com.codiary.backend.domain.member.dto.request.MemberRequestDTO;
import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;

import java.util.List;

public interface MemberCommandService {

    public ApiResponse<String> signUp(MemberRequestDTO.MemberSignUpRequestDTO signUpRequest);

    ApiResponse<String> checkEmailDuplication(String email);

    ApiResponse<String> checkNicknameDuplication(String nickname);

    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> login(MemberRequestDTO.MemberLoginRequestDTO loginRequest);

    String logout(String token, Member member);

    public Member getRequester();

    public ApiResponse<MemberResponseDTO.MemberImageDTO> updateProfileImage(Member member, MemberRequestDTO.MemberProfileImageRequestDTO request);

    public ApiResponse<String> deleteProfileImage(Member member);
}
