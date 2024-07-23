package com.codiary.backend.global.service.MemberService;

import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.converter.MemberConverter;
import com.codiary.backend.global.domain.entity.Follow;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.repository.FollowRepository;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.web.dto.Member.FollowResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;

    @Transactional
    public FollowResponseDto follow(Long toId, Long fromId) {
        if (toId.equals(fromId)) {
            throw new GeneralException(ErrorStatus.MEMBER_SELF_FOLLOW);
        }

        Member fromMember = memberRepository.findById(fromId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Member toMember = memberRepository.findById(toId)
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
            follow = Follow.builder()
                    .followId(follow.getFollowId())
                    .fromMember(follow.getFromMember())
                    .toMember(follow.getToMember())
                    .followStatus(!follow.getFollowStatus())
                    .build();
            if (!follow.getFollowStatus()) {
                fromMember.getFollowings().add(follow);
                toMember.getFollowers().add(follow);
            } else {
                fromMember.getFollowings().remove(follow);
                toMember.getFollowers().remove(follow);
            }
        }
        followRepository.save(follow);
        memberRepository.save(fromMember);
        memberRepository.save(toMember);

        return memberConverter.toManageFollowDto(follow);
    }

    @Transactional
    public Boolean isFollowing(Long toId, Long fromId) {
        if (toId.equals(fromId)) {
            throw new GeneralException(ErrorStatus.MEMBER_SELF_FOLLOW);
        }

        Member fromMember = memberRepository.findById(fromId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Member toMember = memberRepository.findById(toId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        return followRepository.findByFromMemberAndToMember(fromMember, toMember)
                .map(Follow::getFollowStatus)
                .orElse(false);

    }
}
