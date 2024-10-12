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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamFollowService {

    private final TeamRepository teamRepository;
    private final TeamFollowRepository teamFollowRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TeamFollow followTeam(Long toId, Member fromMember) {
        fromMember = memberRepository.findByIdWithFollowedTeams(fromMember.getMemberId()).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Team toTeam = teamRepository.findById(toId).orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        TeamFollow teamFollow;

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

        return teamFollowRepository.save(teamFollow);
    }

    public Boolean isFollowing(Long toId, Member fromMember) {
        // 팀 존재 확인
        Team toTeam = teamRepository.findById(toId).orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        // response
        return teamFollowRepository.findTeamFollowByTeamAndMember(toTeam, fromMember)
                .map(TeamFollow::getFollowStatus)
                .orElse(false);
    }

    public List<Member> getFollowers(Long teamId, Member requester) {
        // 팀 존재 확인
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        // 팀에 소속 멤버인지 확인
        if (!team.getTeamMemberList().contains(requester)) {
            throw new TeamHandler(ErrorStatus.TEAM_MEMBER_ONLY_ACCESS);
        }

        if (team.getFollowers() == null) {
            return new ArrayList<>();
        }

        return team.getFollowers().stream()
                .filter(TeamFollow::getFollowStatus)
                .map(TeamFollow::getMember)
                .collect(Collectors.toList());
    }
}
