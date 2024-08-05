package com.codiary.backend.global.service.MemberService;

import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.domain.entity.Follow;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.repository.FollowRepository;
import com.codiary.backend.global.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Follow follow(Long toId, Member fromMember) {
        if (fromMember == null) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }
        if (toId.equals(fromMember.getMemberId())) {
            throw new GeneralException(ErrorStatus.MEMBER_SELF_FOLLOW);
        }

        fromMember = memberRepository.findByToIdWithFollowings(fromMember.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Member toMember = memberRepository.findByToIdWithFollowers(toId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Follow follow = followRepository.findByFromMemberAndToMember(fromMember, toMember)
                .orElse(null);

        if (follow == null) {
            follow = Follow.builder()
                    .fromMember(fromMember)
                    .toMember(toMember)
                    .followStatus(true)
                    .build();
            fromMember.getFollowings().add(follow);
            toMember.getFollowers().add(follow);
        } else {
            if (follow.getFollowStatus()) {
                follow.update(false);
                fromMember.getFollowings().remove(follow);
                toMember.getFollowers().remove(follow);
            } else {
                follow.update(true);
                fromMember.getFollowings().add(follow);
                toMember.getFollowers().add(follow);
            }
        }
        followRepository.save(follow);

        return follow;
    }

    @Transactional
    public Boolean isFollowing(Long toId, Member fromMember) {
        if(fromMember == null){
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }
        if (toId.equals(fromMember.getMemberId())) {
            throw new GeneralException(ErrorStatus.MEMBER_SELF_FOLLOW);
        }
        Member toMember = memberRepository.findById(toId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        return followRepository.findByFromMemberAndToMember(fromMember, toMember)
                .map(Follow::getFollowStatus)
                .orElse(false);
    }


    @Transactional
    public List<Member> getFollowings(Member member) {
        if (member == null) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        member = memberRepository.findByToIdWithFollowings(member.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Follow> followings = followRepository.findByFromMemberAndFollowStatusTrueOrderByUpdatedAt(member);

        return followings.stream()
                .map(Follow::getToMember)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Member> getFollowers(Member member) {
        if (member == null) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        member = memberRepository.findByToIdWithFollowers(member.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Follow> followers = followRepository.findByToMemberAndFollowStatusTrueOrderByUpdatedAt(member);

        return followers.stream()
                .map(Follow::getFromMember)
                .collect(Collectors.toList());
    }
}
