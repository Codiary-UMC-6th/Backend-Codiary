package com.codiary.backend.domain.team.service;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.team.dto.request.TeamRequestDTO;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.entity.TeamMember;
import com.codiary.backend.domain.team.enumerate.TeamMemberRole;
import com.codiary.backend.domain.team.repository.TeamRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {
    private final TeamRepository teamRepository;

    @Transactional
    public Team createTeam(TeamRequestDTO.CreateTeamDTO request, Member member){
        //business logic: 팀 생성, 팀장 등록
        Team team = Team.builder()
                .name(request.name())
                .intro(request.intro())
                .github(request.github())
                .email(member.getEmail())
                .linkedin(request.linkedIn())
                .instagram(request.instagram())
                .teamMemberList(new ArrayList<>())
                .bannerImage(null)
                .profileImage(null)
                .build();
        addMemberToTeam(member, team, TeamMemberRole.ADMIN);

        //response: 팀 반환
        return teamRepository.save(team);
    }

    public Team getTeamProfile(Long teamId, Member member){
        //business logic: 팀 조회
        Team team = teamRepository.findTeamProfile(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        //reponse: team 반환
        return team;
    }

    public Team getTeam(Long teamId, Member member){
        //business logic: 팀 조회 / 수정 페이지 접근 권환 확인
        Team team = teamRepository.findByTeamIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        if (!teamRepository.isTeamMember(team, member)){
            throw new GeneralException(ErrorStatus.TEAM_ADMIN_UNAUTHORIZED);
        }

        //response: team 반환
        return team;
    }

    @Transactional
    public Team updateTeam(TeamRequestDTO.UpdateTeamDTO request, Long teamId, Member member){
        //validation: team 존재 여부 확인  / 수정 페이지 접근 권환 확인
        Team team = teamRepository.findByTeamIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        if (!teamRepository.isTeamMember(team, member)){
            throw new GeneralException(ErrorStatus.TEAM_ADMIN_UNAUTHORIZED);
        }

        //business logic: team 수정
        team.update(request);

        //response: team 반환
        return teamRepository.save(team);
    }

    private void addMemberToTeam(Member member, Team team, TeamMemberRole role) {
        TeamMember teamMember = TeamMember.builder()
                .member(member)
                .team(team)
                .teamMemberRole(role)
                .build();
        team.getTeamMemberList().add(teamMember);
    }
}
