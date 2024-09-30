package com.codiary.backend.domain.member.service;

import com.codiary.backend.domain.member.entity.Follow;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.member.repository.FollowRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Transactional
    public Follow follow(Long toId, Member fromMember) {
        //Validation: fromMember와 toMember 존재 여부 확인/ 자기 자신 팔로우 불가/ 이미 팔로우 중인지 확인
        fromMember = memberRepository.findByIdWithAndFollowersAndFollowings(fromMember.getMemberId()).orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Member toMember = memberRepository.findByIdWithFollowers(toId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (toId.equals(fromMember.getMemberId())) {
            throw new GeneralException(ErrorStatus.MEMBER_SELF_FOLLOW);
        }

        Follow follow = followRepository.findByFromMemberAndToMember(fromMember, toMember)
                .orElse(null);

        // Business Logic: 팔로잉 여부에 따라 팔로잉 추가/삭제
        if (follow == null) {
            follow = Follow.builder()
                    .fromMember(fromMember)
                    .toMember(toMember)
                    .followStatus(true)
                    .build();
            addFollow(fromMember, toMember, follow);
        } else {
            if (follow.getFollowStatus()) {
                follow.update(false);
                removeFollow(fromMember, toMember, follow);
            } else {
                follow.update(true);
                addFollow(fromMember, toMember, follow);
            }
        }
        followRepository.save(follow);

        // Response: 팔로우 정보 반환
        return follow;
    }

    public Boolean isFollowing(Long toId, Member fromMember) {
        //Validation: toMember 존재 여부 확인/ 자기 자신 팔로우 불가
        if (toId.equals(fromMember.getMemberId())) {
            throw new GeneralException(ErrorStatus.MEMBER_SELF_FOLLOW);
        }
        Member toMember = memberRepository.findById(toId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // Business Logic & Response: 팔로잉 여부 반환
        return followRepository.findByFromMemberAndToMember(fromMember, toMember)
                .map(Follow::getFollowStatus)
                .orElse(false);
    }


    private void addFollow(Member fromMember, Member toMember, Follow follow) {
        fromMember.getFollowings().add(follow);
        toMember.getFollowers().add(follow);
    }

    private void removeFollow(Member fromMember, Member toMember, Follow follow) {
        fromMember.getFollowings().remove(follow);
        toMember.getFollowers().remove(follow);
    }
}
