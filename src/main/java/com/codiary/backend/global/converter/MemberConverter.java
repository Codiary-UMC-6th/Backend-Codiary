package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Follow;
import com.codiary.backend.global.service.MemberService.FollowService;
import com.codiary.backend.global.web.dto.Member.FollowResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public static FollowResponseDto followResponseDto;

    public FollowResponseDto toManageFollowDto(Follow follow) {
        return FollowResponseDto.builder()
                .followId(follow.getFollowId())
                .followerId(follow.getFromMember().getMemberId())
                .followerName(follow.getFromMember().getNickname())
                .followingId(follow.getToMember().getMemberId())
                .followingName(follow.getToMember().getNickname())
                .followStatus(follow.getFollowStatus())
                .build();
    }
}
