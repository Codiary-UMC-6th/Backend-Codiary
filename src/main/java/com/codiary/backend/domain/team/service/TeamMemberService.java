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
        // validation: 팀/멤버 유효성 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));
        Member requestMember = memberRepository.findById(requestMemberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // validation: 요청자 팀원 여부 확인 & 관리자 권한 확인
        TeamMember teamMember = teamMemberRepository.findByTeamAndMember(team, requestMember)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_MEMBER_ONLY_ACCESS));
        if (!teamMember.getTeamMemberRole().equals(TeamMemberRole.ADMIN)) {
            throw new GeneralException(ErrorStatus.TEAM_ADMIN_UNAUTHORIZED);
        }

        // validation: 팀원 초과 여부 확인
        if (teamMemberRepository.countTeamMembersByTeam(team) >= 10) {
            throw new GeneralException(ErrorStatus.TEAM_MEMBER_OVER);
        }

        // validation: 닉네임으로 멤버 조회 및 팀원 여부 파악
        Member newMember = memberRepository.findByNicknameIgnoreCase(request.memberNickName().toString())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        if (teamMemberRepository.existsByTeamAndMember(team, newMember)) {
            throw new GeneralException(ErrorStatus.TEAM_MEMBER_ALREADY_EXISTS);
        }

        //business logic: 팀원 추가
        TeamMember newTeamMember = TeamMember.builder()
                .team(team)
                .member(newMember)
                .teamMemberRole(TeamMemberRole.valueOf(request.memberRole()))
                .build();

        //return
        return teamMemberRepository.save(newTeamMember);
    }

    @Transactional
    public void deleteTeamMember(Long requestMemberId, Long teamId, Long memberId) {
        // validation: 팀/요청자/멤버 유효성 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));
        Member requestMember = memberRepository.findById(requestMemberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // validation: 요청자 팀원 여부 확인 & 관리자 권한 확인
        TeamMember requestTeamMember = teamMemberRepository.findByTeamAndMember(team, requestMember)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_MEMBER_ONLY_ACCESS));
        if (!requestTeamMember.getTeamMemberRole().equals(TeamMemberRole.ADMIN)) {
            throw new GeneralException(ErrorStatus.TEAM_ADMIN_UNAUTHORIZED);
        }

        //validation: 존재하는 팀원인지 유효성 검사
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
