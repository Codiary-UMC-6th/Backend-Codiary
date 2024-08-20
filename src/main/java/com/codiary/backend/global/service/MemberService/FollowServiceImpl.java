package com.codiary.backend.global.service.MemberService;


import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.domain.entity.Follow;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.repository.FollowRepository;
import com.codiary.backend.global.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Follow follow(Long toId, Member fromMember) {
        //Validation: fromMember 존재 여부 확인/ toMember 존재 여부 확인/ 자기 자신 팔로우 불가/ 이미 팔로우 중인지 확인
        if (fromMember == null) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }
        fromMember = memberRepository.findByMemberWithAndFollowersAndFollowings(fromMember).orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Member toMember = memberRepository.findByToIdWithFollowers(toId)
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

        // Response: 팔로우 정보 반환
        return follow;
    }

    @Override
    public Boolean isFollowing(Long toId, Member fromMember) {
        //Validation: fromMember 존재 여부 확인/ toMember 존재 여부 확인/ 자기 자신 팔로우 불가
        if(fromMember == null){
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }
        if (toId.equals(fromMember.getMemberId())) {
            throw new GeneralException(ErrorStatus.MEMBER_SELF_FOLLOW);
        }
        Member toMember = memberRepository.findById(toId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // Response: 팔로잉 여부 반환
        return followRepository.findByFromMemberAndToMember(fromMember, toMember)
                .map(Follow::getFollowStatus)
                .orElse(false);
    }


    @Override
    public List<Member> getFollowings(Member member) {
        //Validation: member 존재 여부 확인
        member = memberRepository.findByToIdWithFollowings(member.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //Business Logic: 팔로잉 리스트 조회
        List<Follow> followings = followRepository.findByFromMemberAndFollowStatusTrueOrderByUpdatedAt(member);

        //Response: 팔로잉 리스트 반환
        return followings.stream()
                .map(Follow::getToMember)
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> getFollowers(Member member) {
        //Validation: member 존재 여부 확인
        member = memberRepository.findByToIdWithFollowers(member.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //Business Logic: 팔로워 리스트 조회
        List<Follow> followers = followRepository.findByToMemberAndFollowStatusTrueOrderByUpdatedAt(member);

        //Response: 팔로워 리스트 반환
        return followers.stream()
                .map(Follow::getFromMember)
                .collect(Collectors.toList());
    }
}
