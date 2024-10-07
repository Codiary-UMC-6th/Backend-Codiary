package com.codiary.backend.domain.team.service;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.enumerate.MemberRole;
import com.codiary.backend.domain.team.dto.request.TeamRequestDTO;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.entity.TeamMember;
import com.codiary.backend.domain.team.repository.TeamRepository;
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
                .email(request.email())
                .linkedin(request.linkedIn())
                .instagram(request.instagram())
                .teamMemberList(new ArrayList<>())
                .bannerImage(null)
                .profileImage(null)
                .build();
        addMemberToTeam(member, team, MemberRole.ADMIN);

        //response: 팀 반환
        return teamRepository.save(team);
    }

    private void addMemberToTeam(Member member, Team team, MemberRole role) {
        TeamMember teamMember = TeamMember.builder()
                .member(member)
                .team(team)
                .teamMemberRole(role)
                .build();
        team.getTeamMemberList().add(teamMember);
    }
}
