package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Follow;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.web.dto.Member.FollowResponseDto;
import com.codiary.backend.global.web.dto.Member.MemberSumResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

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

    public MemberSumResponseDto toFollowResponseDto(Member member) {
        return MemberSumResponseDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .photoUrl(member.getPhotoUrl())
                .build();
    }
}
