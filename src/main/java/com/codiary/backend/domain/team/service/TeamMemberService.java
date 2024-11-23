package com.codiary.backend.domain.team.service;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.team.dto.request.TeamRequestDTO;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.entity.TeamMember;
import com.codiary.backend.domain.team.enumerate.TeamMemberRole;
import com.codiary.backend.domain.team.repository.TeamMemberRepository;
import com.codiary.backend.domain.team.repository.TeamRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamMemberService {
    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TeamMember addTeamMember(Long requestMemberId, Long teamId, TeamRequestDTO.TeamMemberDTO request) {
        //validation: 팀/멤버 유효성 및 팀원 접근인지, 존재하는 닉네임인지, 이미 추가한 팀원인지 유효성 검사
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        Member requestMember = memberRepository.findById(requestMemberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if(!teamMemberRepository.existsByTeamAndMember(team, requestMember)){
            throw new GeneralException(ErrorStatus.TEAM_MEMBER_ONLY_ACCESS);
        }

        if (teamMemberRepository.countTeamMembersByTeam(team) >= 10) {
            throw new GeneralException(ErrorStatus.TEAM_MEMBER_OVER);
        }

        Member newMember = memberRepository.findByNicknameIgnoreCase(request.memberNickName().toString())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if(teamMemberRepository.existsByTeamAndMember(team, newMember)){
            throw new GeneralException(ErrorStatus.TEAM_MEMBER_ALREADY_EXISTS);
        }

        //business logic: 팀원 추가
        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .member(newMember)
                .teamMemberRole(TeamMemberRole.valueOf(request.memberRole()))
                .build();

        //return
        return teamMemberRepository.save(teamMember);
    }

    @Transactional
    public void deleteTeamMember(Long requestMemberId, Long teamId, Long memberId) {
        //validation: 팀/요청자/팀원 유효성 및 요청자가 팀원인지, 존재하는 팀원인지 유효성 검사
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        Member requester = memberRepository.findById(requestMemberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if(!teamMemberRepository.existsByTeamAndMember(team, requester)){
            throw new GeneralException(ErrorStatus.TEAM_MEMBER_NOT_FOUND);
        }

        TeamMember teamMember = teamMemberRepository.findByTeamAndMember(team, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_MEMBER_NOT_FOUND));

        //business logic: 팀원 삭제
        teamMemberRepository.delete(teamMember);
    }

    public Team getTeamMember(Long teamId){
        return teamRepository.findByIdWithTeamMemberList(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));
    }
}
