package com.codiary.backend.domain.alert.service;

import com.codiary.backend.domain.alert.repository.EmitterRepository;
import com.codiary.backend.domain.alert.repository.NewPostAlertRepository;
import com.codiary.backend.domain.member.entity.Follow;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.FollowRepository;
import com.codiary.backend.domain.post.converter.PostConverter;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class AlertService {
    private static final Long TIMEOUT = 24 * 60 * 60 * 1000L;

    private final NewPostAlertRepository postAlertRepository;
    private final FollowRepository followRepository;
    private final EmitterRepository emitterRepository;

    public SseEmitter connect(Long memberId) {
        SseEmitter emitter = emitterRepository.save(memberId, new SseEmitter(TIMEOUT));

        try {
            emitter.send(SseEmitter.event()
                    .name("on connect")
                    .data("연결되었습니다!")
            );
        } catch (IOException e) {
            // 해당 emitter 를 삭제
        }

        return emitter;
    }

    public void sendNewPostAlert(Post post) {
        // 보내야 할 대상 탐색 ( 공개 범위 )
        if (!post.getPostAccess().equals(PostAccess.ENTIRE)) {
            return;
        }

        // 작성자 추출 및 작성자 follow 상태인 사용자 추출
        Member poster = post.getMember();
        List<Member> followList = followRepository.findByToMemberAndFollowStatusTrue(poster)
                .stream().map(Follow::getFromMember).toList();
        Map<Long, SseEmitter> emitterList = emitterRepository.getEmittersByMembersId(
                followList.stream()
                        .filter(member -> true) // 이후에 알람 on off 유무 확인
                        .map(Member::getMemberId).toList()
        );

        // 알람 보내기
        emitterList.forEach(
                (key, emitter) -> {
                    try {
                        emitter.send(SseEmitter.event().name("following member's new post")
                                .data(PostConverter.toSimplePostResponseDto(post)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    private void send(SseEmitter emitter, String name, Object data, Long emitterId) {
        try {
            emitter.send(SseEmitter.event()
                    .name(name)
                    .data(data)
            );
        } catch (IOException e) {
            // 해당 emitter 를 삭제
        }
    }
}
