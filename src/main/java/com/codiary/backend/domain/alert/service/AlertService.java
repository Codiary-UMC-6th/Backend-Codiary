package com.codiary.backend.domain.alert.service;

import com.codiary.backend.domain.alert.entity.AlertEvent;
import com.codiary.backend.domain.alert.entity.EventCategory;
import com.codiary.backend.domain.alert.repository.EmitterRepository;
import com.codiary.backend.domain.comment.converter.CommentConverter;
import com.codiary.backend.domain.comment.entity.Comment;
import com.codiary.backend.domain.member.converter.MemberConverter;
import com.codiary.backend.domain.member.entity.Follow;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.FollowRepository;
import com.codiary.backend.domain.post.converter.PostConverter;
import com.codiary.backend.domain.post.entity.Bookmark;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.codiary.backend.domain.team.converter.TeamConverter;
import com.codiary.backend.domain.team.entity.TeamFollow;
import com.codiary.backend.domain.team.entity.TeamMember;
import com.codiary.backend.domain.team.enumerate.TeamMemberRole;
import com.codiary.backend.domain.team.repository.TeamFollowRepository;
import com.codiary.backend.domain.team.repository.TeamMemberRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class AlertService {
    private static final Long TIMEOUT = 24 * 60 * 60 * 1000L;

    private final FollowRepository followRepository;
    private final EmitterRepository emitterRepository;
    private final TeamFollowRepository teamFollowRepository;
    private final TeamMemberRepository teamMemberRepository;

    public SseEmitter connect(Long memberId, String lastEvent) {
        SseEmitter emitter = emitterRepository.save(memberId, new SseEmitter(TIMEOUT));

        AlertEvent event = AlertEvent.builder()
                .eventId(String.valueOf(System.currentTimeMillis()))
                .kindOfEvent(EventCategory.CONNECT)
                .data("연결되었습니다!")
                .build();
        send(emitter, event, memberId);

        // 쌓여있는 알림이 있다면 전송
        sendLostEvent(memberId, emitter, lastEvent);

        return emitter;
    }

    public void disconnect(Long memberId) {
        SseEmitter emitter = emitterRepository.getEmitterByMemberId(memberId);

        AlertEvent event = AlertEvent.builder()
                .eventId(String.valueOf(System.currentTimeMillis()))
                .kindOfEvent(EventCategory.DISCONNECT)
                .data("연결이 해지되었습니다!")
                .build();
        send(emitter, event, memberId);

        emitterRepository.deleteAllEmittersAboutMember(memberId);
    }

    public void sendBookmarkAlert(Bookmark bookmark) {
        // 알람 on/off 확인

        // 작성자 id 구하기
        Long memberId = bookmark.getPost().getMember().getMemberId();

        // 이벤트 저장
        AlertEvent event = saveEvent(
                EventCategory.BOOKMARK,
                PostConverter.toBookmarkDTO(bookmark),
                new ArrayList<>() {
                    {
                        add(memberId);
                    }
                }
        );

        // 게시물 작성자에게 알림
        SseEmitter emitter = emitterRepository.getEmitterByMemberId(memberId);
        send(emitter, event, memberId);
    }

    public void sendCommentAlert(Comment comment) {
        // 알람 on/off 확인

        // 다이어리 작성자 id 구하기
        // 댓글이면 post 가져오고 대댓글이면 댓글 타고 post 가져옴
        Long memberId = comment.getPost() != null
                ? comment.getPost().getMember().getMemberId()
                : comment.getParent().getPost().getMember().getMemberId();

        // 본인의 다이어리인 경우 알람 X
        if (memberId.equals(comment.getMember().getMemberId())) {
            return;
        }

        // 작성자의 emitter 조회
        SseEmitter emitter = emitterRepository.getEmitterByMemberId(memberId);

        // 이벤트 저장
        AlertEvent event = saveEvent(
                EventCategory.COMMENT,
                CommentConverter.toCommentResponseDto(comment),
                new ArrayList<>() {
                    {
                        add(memberId);
                    }
                }
        );

        // 알림 전송
        send(emitter, event, memberId);
    }

    public void sendTeamAppendAlert(TeamMember teamMember) {
        Long memberId = teamMember.getMember().getMemberId();

        SseEmitter emitter = emitterRepository.getEmitterByMemberId(memberId);

        // 이벤트 저장
        AlertEvent event = saveEvent(
                EventCategory.JOIN_TEAM,
                TeamConverter.toTeamMemberResponseDTO(teamMember),
                new ArrayList<>() {
                    {
                        add(memberId);
                    }
                }
        );

        send(emitter, event, memberId);
    }

    public void sendTeamExiledAlert(Long teamId, Long memberId) {
        SseEmitter emitter = emitterRepository.getEmitterByMemberId(memberId);

        // 이벤트 저장
        AlertEvent event = saveEvent(EventCategory.KICKED_OUT_TEAM, "id: " + teamId + " 팀에서 추방되셨습니다.",
                new ArrayList<>() {
            {
                add(memberId);
            }
        });

        send(emitter, event, memberId);
    }

    public void sendTeamFollowAlert(TeamFollow teamFollow) {
        // 팔로우 요청인지 확인
        if (teamFollow.getFollowStatus().equals(false)) {
            return;
        }

        // 팀 멤버 id list 구하기
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(teamFollow.getTeam());
        List<Long> membersId = teamMembers.stream()
                .filter(teamMember -> true) // 알람 on/off 확인
                .filter(teamMember -> teamMember.getTeamMemberRole().equals(TeamMemberRole.ADMIN))
                .map(teamMember -> teamMember.getMember().getMemberId())
                .toList();

        // 이벤트 저장
        AlertEvent event = saveEvent(
                EventCategory.TEAM_FOLLOW,
                TeamConverter.toTeamFollowResponseDTO(teamFollow),
                membersId
        );

        // 팔로우 알림 전송
        Map<Long, SseEmitter> emitters = emitterRepository.getEmittersByMembersId(membersId);
        emitters.forEach(
                (key, emitter) -> {
                    send(emitter, event, key);
                }
        );
    }

    public void sendMemberFollowAlert(Follow follow) {
        // 팔로우 요청인지 확인
        if (follow.getFollowStatus().equals(false)) {
            return;
        }

        // 이벤트 저장
        AlertEvent event = saveEvent(EventCategory.FOLLOW, MemberConverter.toFollowDto(follow), new ArrayList<>() {
            {
                add(follow.getToMember().getMemberId());
            }
        });

        // validation : 요청 on/off 상태 확인

        // 팔로우 알림 전송
        SseEmitter emitter = emitterRepository.getEmitterByMemberId(follow.getToMember().getMemberId());
        send(emitter, event, follow.getToMember().getMemberId());
    }

    public void sendNewPostAlert(Post post) {
        // 작성자 팔로워들에게 알림 + (팀에 속한 글이라면) 팀 팔로워들에게 알림

        // 보내야 할 대상 탐색 ( 공개 범위 )
        if (!post.getPostAccess().equals(PostAccess.ENTIRE)) {
            return;
        }

        // 작성자 추출 및 작성자 follow 상태인 사용자 추출
        Member poster = post.getMember();
        List<Member> followList = followRepository.findByToMemberAndFollowStatusTrue(poster)
                .stream().map(Follow::getFromMember).toList();
        List<Long> followIdList = followList.stream()
                        .filter(member -> true) // 이후에 알람 on off 유무 확인
                .map(Member::getMemberId).toList();
        Map<Long, SseEmitter> emitters = emitterRepository.getEmittersByMembersId(followIdList);

        // 이벤트 저장
        AlertEvent event = saveEvent(
                EventCategory.FOLLOWING_MEMBER_NEW_POST,
                PostConverter.toSimplePostResponseDto(post),
                followIdList
        );

        // 알람 보내기
        emitters.forEach(
                (key, emitter) -> {
                    send(emitter, event, key);
                }
        );

        // 팀 소속 게시물인 경우
        if (post.getTeam() != null) {
            // 팀 팔로워들 추출 후, 알람을 켜놓은 사람들 id 리스트를 바탕으로 emitter 받아오기
            List<Long> memberIdList = teamFollowRepository.findFollowersByTeamId(post.getTeam().getTeamId())
                            .stream().map(TeamFollow::getMember)
                            .filter(member -> true) // 이후에 알람 on off 유무 확인
                    .map(Member::getMemberId).toList();
            Map<Long, SseEmitter> emitterMap = emitterRepository.getEmittersByMembersId(memberIdList);

            // 이벤트 저장
            AlertEvent teamEvent = saveEvent(
                    EventCategory.FOLLOWING_TEAM_NEW_POST,
                    PostConverter.toSimplePostResponseDto(post),
                    memberIdList
            );

            emitterMap.forEach(
                    (key, emitter) -> {
                        send(emitter, teamEvent, key);
                    }
            );
        }
    }

    private void send(SseEmitter emitter, AlertEvent event, Long emitterId) {
        if (emitter == null) {
            return;
        }

        try {
            emitter.send(SseEmitter.event()
                    .id(event.getEventId())
                    .name(event.getKindOfEvent().name())
                    .data(event.getData())
            );
        } catch (IOException e) {
            emitterRepository.deleteAllEmittersAboutMember(emitterId);
        }
    }

    private void send(SseEmitter emitter, List<AlertEvent> eventList, Long emitterId) {
        if (emitter == null) {
            return;
        }

        try {
            emitter.send(SseEmitter.event()
                    .name("LOST_EVENTS")
                    .data(eventList)
            );
        } catch (IOException e) {
            emitterRepository.deleteAllEmittersAboutMember(emitterId);
        }
    }

    private AlertEvent saveEvent(EventCategory kindOfEvent, Object data, List<Long> receiverList) {
        AlertEvent event = AlertEvent.builder()
                .kindOfEvent(kindOfEvent)
                .eventId(String.valueOf(System.currentTimeMillis()))
                .alertMemberIdList(receiverList)
                .data(data)
                .build();
        return emitterRepository.save(event);
    }

    private void sendLostEvent(Long memberId, SseEmitter emitter, String lastEventId) {
        // 마지막 수신 이벤트 값이 없으면 무시
        System.out.println("im in!" + lastEventId);
        if (lastEventId == null || lastEventId.isEmpty()) {
            return;
        }

        System.out.println("im in!");
        // 해당 멤버에게 가야할 놓친 이벤트 탐색
        List<AlertEvent> eventList = emitterRepository.getEventsAfterLastEvent(lastEventId, memberId);

        System.out.println("size:" + eventList.size());

        // 알람 전송
        send(emitter, eventList, memberId);
//        for (AlertEvent event : eventList) {
//            // 각 이벤트에 대한 알람 on/off 확인 필요
//            System.out.println(memberId);
//            send(emitter, event, memberId);
//        }
    }
}
