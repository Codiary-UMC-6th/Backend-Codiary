package com.codiary.backend.domain.member.service;

import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
@Getter
public class MemberQueryServiceImpl implements MemberQueryService {
    private final MemberRepository memberRepository;

    @Override
    public ApiResponse<MemberResponseDTO.MemberImageDTO> getProfileImage(Long memberId) {
        Member user = memberRepository.findById(memberId).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        String url = (user.getImage() != null) ? user.getImage().getImageUrl() : "";
        MemberResponseDTO.MemberImageDTO response = new MemberResponseDTO.MemberImageDTO(url);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, response);
    }

}
