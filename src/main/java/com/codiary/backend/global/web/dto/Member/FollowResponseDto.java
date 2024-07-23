package com.codiary.backend.global.web.dto.Member;

import lombok.Builder;

@Builder
public record FollowResponseDto(
        Long followId,
        Long followerId,
        String followerName,
        Long followingId,
        String followingName,
        Boolean followStatus
) {
}
