package com.codiary.backend.global.web.dto.Member;

import com.codiary.backend.global.domain.entity.Follow;
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
