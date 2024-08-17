package com.codiary.backend.global.service.MemberService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.mapping.MemberCategory;
import com.codiary.backend.global.domain.enums.TechStack;
import com.codiary.backend.global.web.dto.Member.MemberRequestDTO;
import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;

import java.util.List;

public interface MemberCommandService {

    public ApiResponse<String> signUp(MemberRequestDTO.MemberSignUpRequestDTO signUpRequest);

    ApiResponse<String> checkEmailDuplication(String email);

    ApiResponse<String> checkNicknameDuplication(String nickname);

    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> login(MemberRequestDTO.MemberLoginRequestDTO loginRequest);

    String logout(String token, Member member);

    // 회원별 관심 카테고리탭 리스트 조회
    List<MemberCategory> getMemberCategoryList(Long memberId);

    public Member getRequester();

    public ApiResponse<MemberResponseDTO.MemberImageDTO> updateProfileImage(Member member, MemberRequestDTO.MemberProfileImageRequestDTO request);

    public ApiResponse<String> deleteProfileImage(Member member);

    public Member setTechStacks(Long memberId, TechStack techstack);

    public MemberResponseDTO.ProjectsDTO setProjects(Long memberId, String projects);

    public Member updateMemberInfo(Member member, MemberRequestDTO.MemberInfoRequestDTO request);
}
