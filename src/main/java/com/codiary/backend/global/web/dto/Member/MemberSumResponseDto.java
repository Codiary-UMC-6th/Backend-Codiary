package com.codiary.backend.global.web.dto.Member;

import lombok.Builder;

@Builder
public record MemberSumResponseDto(
        Long memberId,
        String nickname,
        String photoUrl
) {
}
