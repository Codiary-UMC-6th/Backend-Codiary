package com.codiary.backend.global.service.MemberService;

import com.codiary.backend.global.domain.entity.Follow;
import com.codiary.backend.global.domain.entity.Member;

import java.util.List;

public interface FollowService {

    Follow follow(Long toId, Member fromMember);
    Boolean isFollowing(Long toId, Member fromMember);
    List<Member> getFollowings(Member member);
    List<Member> getFollowers(Member member);
}
