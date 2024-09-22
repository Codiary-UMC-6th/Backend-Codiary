package com.codiary.backend.domain.member.service;

import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.codiary.backend.global.apiPayload.ApiResponse;

public interface MemberQueryService {

    ApiResponse<MemberResponseDTO.MemberImageDTO> getProfileImage(Long memberId);

}
