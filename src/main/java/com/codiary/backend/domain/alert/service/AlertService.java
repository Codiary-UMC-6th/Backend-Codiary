package com.codiary.backend.domain.alert.service;

import com.codiary.backend.domain.alert.entity.AlertEvent;
import com.codiary.backend.domain.alert.entity.EventCategory;
import com.codiary.backend.domain.alert.entity.EventReceive;
import com.codiary.backend.domain.alert.repository.EmitterRepository;
import com.codiary.backend.domain.alert.repository.EventReceiveRepository;
import com.codiary.backend.domain.comment.converter.CommentConverter;
import com.codiary.backend.domain.comment.entity.Comment;
import com.codiary.backend.domain.member.converter.MemberConverter;
import com.codiary.backend.domain.member.entity.Follow;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.FollowRepository;
import com.codiary.backend.domain.member.repository.MemberRepository;
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
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
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
    private final EventReceiveRepository eventReceiveRepository;
    private final TeamFollowRepository teamFollowRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;

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
        // validation : 작성자 구하기, 알람 on/off 확인
        Member member = bookmark.getPost().getMember();
        if (eventReceiveRepository.existsByMemberAndEventCategoryAndStatusFalse(member, EventCategory.BOOKMARK)) {
            return;
        }

        // business logic : 이벤트 저장 및 알림
        Long memberId = member.getMemberId();

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
        // validation : 다이어리 작성자 구하기, 알람 on/off 확인, 작성자 댓글인지 확인
        Member member = comment.getPost() != null
                ? comment.getPost().getMember()
                : comment.getParent().getPost().getMember();
        if (eventReceiveRepository.existsByMemberAndEventCategoryAndStatusFalse(member, EventCategory.COMMENT)) {
            return;
        }
        if (member.equals(comment.getMember())) {
            return;
        }

        // business logic : 이벤트 저장 및 알림
        Long memberId = member.getMemberId();

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

        // 게시물 작성자에게 알림
        SseEmitter emitter = emitterRepository.getEmitterByMemberId(memberId);
        send(emitter, event, memberId);
    }

    public void sendTeamAppendAlert(TeamMember teamMember) {
        // validation : 합류한 팀원 구하기, 알람 on/off 확인
        Member member = teamMember.getMember();
        if (eventReceiveRepository.existsByMemberAndEventCategoryAndStatusFalse(member, EventCategory.JOIN_TEAM)) {
            return;
        }

        // business logic : 이벤트 저장 및 알림
        Long memberId = member.getMemberId();

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

        // 합류한 사용자에게 알림
        SseEmitter emitter = emitterRepository.getEmitterByMemberId(memberId);
        send(emitter, event, memberId);
    }

    public void sendTeamExiledAlert(Long teamId, Long memberId) {
        // validation : 추방된 팀원 구하기, 알람 on/off 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (eventReceiveRepository
                .existsByMemberAndEventCategoryAndStatusFalse(member, EventCategory.KICKED_OUT_TEAM)) {
            return;
        }

        // business logic : 이벤트 저장 및 알림

        // 이벤트 저장
        AlertEvent event = saveEvent(EventCategory.KICKED_OUT_TEAM, "id: " + teamId + " 팀에서 추방되셨습니다.",
                new ArrayList<>() {
            {
                add(memberId);
            }
        });

        // 추방된 사용자에게 알림
        SseEmitter emitter = emitterRepository.getEmitterByMemberId(memberId);
        send(emitter, event, memberId);
    }

    public void sendTeamFollowAlert(TeamFollow teamFollow) {
        // validation : 팔로우 요청인지 확인
        if (teamFollow.getFollowStatus().equals(false)) {
            return;
        }

        // validation & business logic : 알람 on/off 확인 및 관리자 여부 확인 & 팀 멤버 id list 구하기
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(teamFollow.getTeam());
        List<Long> membersId = teamMembers.stream()
                // 알람 on/off 확인
                .filter(teamMember ->
                        !eventReceiveRepository.existsByMemberAndEventCategoryAndStatusFalse(
                                teamMember.getMember(),
                                EventCategory.TEAM_FOLLOW
                        )
                )
                // 관리자 여부 확인
                .filter(teamMember -> teamMember.getTeamMemberRole().equals(TeamMemberRole.ADMIN))
                .map(teamMember -> teamMember.getMember().getMemberId())
                .toList();

        // business logic : 이벤트 저장 및 알림

        // 이벤트 저장
        AlertEvent event = saveEvent(
                EventCategory.TEAM_FOLLOW,
                TeamConverter.toTeamFollowResponseDTO(teamFollow),
                membersId
        );

        // 팀 관리자들에게 팀 팔로우 알림 전송
        Map<Long, SseEmitter> emitters = emitterRepository.getEmittersByMembersId(membersId);
        emitters.forEach(
                (key, emitter) -> {
                    send(emitter, event, key);
                }
        );
    }

    public void sendMemberFollowAlert(Follow follow) {
        // validation : 팔로우 요청인지 확인, 팔로우 대상 사용자 구하기, 알람 on/off 확인
        if (follow.getFollowStatus().equals(false)) {
            return;
        }
        Member member = follow.getToMember();
        if (eventReceiveRepository.existsByMemberAndEventCategoryAndStatusFalse(member, EventCategory.FOLLOW)) {
            return;
        }

        // business logic : 이벤트 저장 및 알림

        // 이벤트 저장
        AlertEvent event = saveEvent(EventCategory.FOLLOW, MemberConverter.toFollowDto(follow), new ArrayList<>() {
            {
                add(follow.getToMember().getMemberId());
            }
        });

        // 팔로우 알림 전송
        SseEmitter emitter = emitterRepository.getEmitterByMemberId(follow.getToMember().getMemberId());
        send(emitter, event, follow.getToMember().getMemberId());
    }

    public void sendNewPostAlert(Post post) {
        // 작성자 팔로워들에게 알림 + (팀에 속한 글이라면) 팀 팔로워들에게 알림

        // 전체 사용자 대상으로 한 경우에만 알림
        if (post.getPostAccess().equals(PostAccess.ENTIRE)) {

            // 사용자 팔로워들에게 알림
            sendMemberNewPostAlert(post);

            // 팀 소속 게시물인 경우 팀 팔로워들에게 알림
            if (post.getTeam() != null) {
                sendTeamNewPostAlertToTeamFollowers(post);
            }
        }

        // 팀 소속 게시물인 경우 팀 멤버에게 새 글 알림
        if (post.getTeam() != null) {
            sendTeamNewPostAlertToTeamMembers(post);
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
        if (lastEventId == null || lastEventId.isEmpty()) {
            return;
        }

        // 해당 멤버에게 가야할 놓친 이벤트 탐색
        List<AlertEvent> eventList = emitterRepository.getEventsAfterLastEvent(lastEventId, memberId);

        // 알람 전송
        for (AlertEvent event : eventList) {
            // 각 이벤트에 대한 알람 on/off 확인 필요
            System.out.println(event.getEventId());
            send(emitter, event, memberId);
        }
    }

    public EventReceive alertOnOff(EventCategory eventCategory, Long memberId) {
        // validation : 유저 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // business logic
        EventReceive eventReceive
                = eventReceiveRepository.findByMemberAndEventCategory(member, eventCategory).orElse(null);

        if (eventReceive != null) {
            // 알람 수신 여부 관련 entity 존재하는 경우 (상태 전환)
            eventReceive.setStatus(!eventReceive.getStatus());
        } else {
            // 알람 수신 여부 관련 entity 존재하지 않는 경우 (수신 여부 false 로 생성)
            eventReceive = EventReceive.builder()
                    .member(member)
                    .category(eventCategory)
                    .status(false)
                    .build();
        }

        // response
        return eventReceiveRepository.save(eventReceive);
    }

    private void sendMemberNewPostAlert(Post post) {

        // validation & business logic : 팔로워들 알람 on/off 확인 & 수신 대상 id 리스트 구하기
        List<Long> receiverIdList = followRepository.findByToMemberAndFollowStatusTrue(post.getMember())
                .stream().map(Follow::getFromMember)
                // 알람 on/off 확인
                .filter(
                        member -> !eventReceiveRepository.existsByMemberAndEventCategoryAndStatusFalse(
                                member,
                                EventCategory.FOLLOWING_MEMBER_NEW_POST
                        )
                )
                .map(Member::getMemberId).toList();

        // business logic : 이벤트 저장 및 알림

        // 이벤트 저장
        AlertEvent event = saveEvent(
                EventCategory.FOLLOWING_MEMBER_NEW_POST,
                PostConverter.toPostPreviewDTO(post),
                receiverIdList
        );

        // 팔로워들에게 알람 전송
        Map<Long, SseEmitter> emitters = emitterRepository.getEmittersByMembersId(receiverIdList);
        emitters.forEach(
                (key, emitter) -> {
                    send(emitter, event, key);
                }
        );
    }

    private void sendTeamNewPostAlertToTeamFollowers(Post post) {

        // validation & business logic : 팔로워들 알람 on/off 확인 & 수신 대상 id 리스트 구하기
        List<Long> receiverIdList = teamFollowRepository.findFollowersByTeamId(post.getTeam().getTeamId())
                .stream().map(TeamFollow::getMember)
                // 알람 on/off 확인
                .filter(
                        member -> !eventReceiveRepository.existsByMemberAndEventCategoryAndStatusFalse(
                                member,
                                EventCategory.FOLLOWING_TEAM_NEW_POST
                        )
                )
                .map(Member::getMemberId).toList();

        // business logic : 이벤트 저장 및 알림

        // 이벤트 저장
        AlertEvent event = saveEvent(
                EventCategory.FOLLOWING_TEAM_NEW_POST,
                PostConverter.toPostPreviewDTO(post),
                receiverIdList
        );

        // 팀 팔로워들에게 알림 전송
        Map<Long, SseEmitter> emitterMap = emitterRepository.getEmittersByMembersId(receiverIdList);
        emitterMap.forEach(
                (key, emitter) -> {
                    send(emitter, event, key);
                }
        );
    }

    private void sendTeamNewPostAlertToTeamMembers(Post post) {

        // validation & business logic : 팀 소속 멤버들 알람 on/off 확인 & 수신 대상 id 리스트 구하기
        List<Long> receiverIdList = teamMemberRepository.findTeamMembersByTeam(post.getTeam())
                .stream().map(TeamMember::getMember)
                // 알람 on/off 확인
                .filter(
                        member -> !eventReceiveRepository.existsByMemberAndEventCategoryAndStatusFalse(
                                member,
                                EventCategory.MY_TEAM_NEW_POST
                        )
                )
                .map(Member::getMemberId).toList();

        // business logic : 이벤트 저장 및 알림

        // 이벤트 저장
        AlertEvent event = saveEvent(
                EventCategory.MY_TEAM_NEW_POST,
                PostConverter.toPostPreviewDTO(post),
                receiverIdList
        );

        // 팀 멤버들에게 알림 전송
        Map<Long, SseEmitter> emitterMap = emitterRepository.getEmittersByMembersId(receiverIdList);
        emitterMap.forEach(
                (key, emitter) -> {
                    send(emitter, event, key);
                }
        );
    }
}
