package com.codiary.backend.global.web.dto.Member;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FollowResponseDto(
        Long followId,
        Long followerId,
        String followerName,
        Long followingId,
        String followingName,
        Boolean followStatus
) {
}
