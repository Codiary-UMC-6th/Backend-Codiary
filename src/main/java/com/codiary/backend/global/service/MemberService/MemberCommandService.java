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

    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> login(MemberRequestDTO.MemberLoginRequestDTO loginRequest);

    // 회원별 관심 카테고리탭 리스트 조회
    List<MemberCategory> getMemberCategoryList(Long memberId);

    public Member getRequester();

    public ApiResponse<MemberResponseDTO.MemberImageDTO> setProfileImage(Member member, MemberRequestDTO.MemberProfileImageRequestDTO request);

    public Member setTechStacks(Long memberId, TechStack techstack);

    public MemberResponseDTO.ProjectsDTO setProjects(Long memberId, String projects);

    public Member updateMemberInfo(Member member, MemberRequestDTO.MemberInfoRequestDTO request);
}
