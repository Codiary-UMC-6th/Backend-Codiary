package com.codiary.backend.domain.alert.service;

import com.codiary.backend.domain.alert.repository.EmitterRepository;
import com.codiary.backend.domain.alert.repository.NewPostAlertRepository;
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
    private final TeamMemberRepository teamMemberRepository;

    public SseEmitter connect(Long memberId) {
        SseEmitter emitter = emitterRepository.save(memberId, new SseEmitter(TIMEOUT));

        send(emitter, "On Connect", "연결되었습니다!", memberId);

        return emitter;
    }

    public void disconnect(Long memberId) {
        SseEmitter emitter = emitterRepository.getEmitterByMemberId(memberId);
        send(emitter, "disconnect", "알람 연결이 해지되었습니다.", memberId);
        emitterRepository.deleteAllEmittersAboutMember(memberId);
    }

    public void sendBookmarkAlert(Bookmark bookmark) {
        // 알람 on/off 확인

        // 작성자 id 구하기
        Long memberId = bookmark.getPost().getMember().getMemberId();

        // 게시물 작성자에게 알림
        SseEmitter emitter = emitterRepository.getEmitterByMemberId(memberId);
        send(emitter, "Bookmark", PostConverter.toBookmarkDTO(bookmark), memberId);
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

        // 게시물 작성자에게 알림
        SseEmitter emitter = emitterRepository.getEmitterByMemberId(memberId);
        send(emitter, "Comment", CommentConverter.toCommentResponseDto(comment), memberId);
    }

    public void sendTeamAppendAlert(TeamMember teamMember) {
        Long memberId = teamMember.getMember().getMemberId();

        SseEmitter emitter = emitterRepository.getEmitterByMemberId(memberId);
        send(emitter, "Join in Team", TeamConverter.toTeamMemberResponseDTO(teamMember), memberId);
    }

    public void sendTeamExiledAlert(Long teamId, Long memberId) {
        SseEmitter emitter = emitterRepository.getEmitterByMemberId(memberId);
        send(emitter, "Kicked out of Team", "id: " + teamId + " 팀에서 추방되셨습니다.", memberId);
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

        // 팔로우 알림 전송
        Map<Long, SseEmitter> emitters = emitterRepository.getEmittersByMembersId(membersId);
        emitters.forEach(
                (key, emitter) -> {
                    send(emitter, "team follow", TeamConverter.toTeamFollowResponseDTO(teamFollow), key);
                }
        );
    }

    public void sendMemberFollowAlert(Follow follow) {
        // 팔로우 요청인지 확인
        if (follow.getFollowStatus().equals(false)) {
            return;
        }

        // validation : 요청 on/off 상태 확인

        // 팔로우 알림 전송
        SseEmitter emitter = emitterRepository.getEmitterByMemberId(follow.getToMember().getMemberId());
        send(emitter, "follow", MemberConverter.toFollowDto(follow), follow.getToMember().getMemberId());
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
        if (emitter == null) {
            return;
        }

        try {
            emitter.send(SseEmitter.event()
                    .name(name)
                    .data(data)
            );
        } catch (IOException e) {
            emitterRepository.deleteAllEmittersAboutMember(emitterId);
        }
    }
}
