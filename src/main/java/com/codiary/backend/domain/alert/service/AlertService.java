package com.codiary.backend.domain.alert.service;

import com.codiary.backend.domain.alert.repository.EmitterRepository;
import com.codiary.backend.domain.alert.repository.NewPostAlertRepository;
import com.codiary.backend.domain.member.entity.Follow;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.FollowRepository;
import com.codiary.backend.domain.post.converter.PostConverter;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.codiary.backend.domain.team.entity.TeamFollow;
import com.codiary.backend.domain.team.repository.TeamFollowRepository;
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
    private final TeamFollowRepository teamFollowRepository;

    public SseEmitter connect(Long memberId) {
        SseEmitter emitter = emitterRepository.save(memberId, new SseEmitter(TIMEOUT));

        send(emitter, "On Connect", "연결되었습니다!", memberId);

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
        Map<Long, SseEmitter> emitters = emitterRepository.getEmittersByMembersId(
                followList.stream()
                        .filter(member -> true) // 이후에 알람 on off 유무 확인
                        .map(Member::getMemberId).toList()
        );

        // 알람 보내기
        emitters.forEach(
                (key, emitter) -> {
                    send(emitter, "following member's new post", PostConverter.toSimplePostResponseDto(post), key);
                }
        );

        if (post.getTeam() != null) {
            // 팀 팔로워들 추출 후, 알람을 켜놓은 사람들 id 리스트를 바탕으로 emitter 받아오기
            Map<Long, SseEmitter> emitterMap = emitterRepository.getEmittersByMembersId(
                    teamFollowRepository.findFollowersByTeamId(post.getTeam().getTeamId())
                            .stream().map(TeamFollow::getMember)
                            .filter(member -> true) // 이후에 알람 on off 유무 확인
                            .map(Member::getMemberId).toList()
            );

            emitterMap.forEach(
                    (key, emitter) -> {
                        send(emitter, "following team's new post", PostConverter.toSimplePostResponseDto(post), key);
                    }
            );
        }
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
