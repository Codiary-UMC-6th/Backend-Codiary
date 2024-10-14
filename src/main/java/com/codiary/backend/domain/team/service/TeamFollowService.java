package com.codiary.backend.domain.team.service;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.entity.TeamFollow;
import com.codiary.backend.domain.team.repository.TeamFollowRepository;
import com.codiary.backend.domain.team.repository.TeamRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.codiary.backend.global.apiPayload.exception.handler.TeamHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamFollowService {

    private final TeamRepository teamRepository;
    private final TeamFollowRepository teamFollowRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TeamFollow followTeam(Long toId, Member fromMember) {
        // validation: member 존재 여부 확인 및 팀 존재 여부 확인
        fromMember = memberRepository.findByIdWithFollowedTeams(fromMember.getMemberId()).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Team toTeam = teamRepository.findById(toId).orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        TeamFollow teamFollow;

        // business logic: 팔로우/언팔로우
        if (teamFollowRepository.existsByTeamAndAndMember(toTeam, fromMember)) {
            // 관계 있음
            teamFollow = teamFollowRepository.findTeamFollowByTeamAndMember(toTeam, fromMember).get();

            if (teamFollow.getFollowStatus()) {
                // 관계가 팔로우 상태 -> 언팔로우 상태
                toTeam.getFollowers().remove(teamFollow);
                fromMember.getFollowedTeams().remove(teamFollow);
            } else {
                // 언팔로우 상태 -> 팔로우 상태
                toTeam.getFollowers().add(teamFollow);
                fromMember.getFollowedTeams().add(teamFollow);
            }
            teamFollow.update();
        } else {
            // 관계 없음 -> 팔로우 상태 생성
            teamFollow = TeamFollow.builder()
                    .team(toTeam)
                    .member(fromMember)
                    .build();
            toTeam.getFollowers().add(teamFollow);
            fromMember.getFollowedTeams().add(teamFollow);
        }

        // response: 생성/갱신된 teamFollow 반환
        return teamFollowRepository.save(teamFollow);
    }

    public Boolean isFollowing(Long toId, Member fromMember) {
        // validation: 팀 존재 확인
        Team toTeam = teamRepository.findById(toId).orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        // business logic & response: 팀 팔로우 여부 반환
        return teamFollowRepository.findTeamFollowByTeamAndMember(toTeam, fromMember)
                .map(TeamFollow::getFollowStatus)
                .orElse(false);
    }

    public List<TeamFollow> getFollowers(Long teamId, Member requester) {
        // validation: 팀 존재 확인 & 팀 소속 여부 확인
        Team team = teamRepository.findByIdWithFollowers(teamId).orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        if (!teamRepository.isTeamMember(team, requester)) {
            throw new TeamHandler(ErrorStatus.TEAM_MEMBER_ONLY_ACCESS);
        }

        // business logic: follower 조회
        List<TeamFollow> teamFollowers = teamFollowRepository.findFollowersByTeamId(teamId);

        // response: 조회한 팔로워 반환
        return teamFollowers;
    }
}
