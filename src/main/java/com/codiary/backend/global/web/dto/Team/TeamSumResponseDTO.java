package com.codiary.backend.global.web.dto.Team;

public record TeamSumResponseDTO(
    Long memberId,
    String nickname,
    String photoUrl
) {
}
