package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.mapping.TeamMember;
import com.codiary.backend.global.web.dto.TeamMember.TeamMemberResponseDTO;

public class TeamMemberConverter {

  public static TeamMemberResponseDTO.TeamMemberDTO toTeamMemberDTO(TeamMember teamMember) {
    return TeamMemberResponseDTO.TeamMemberDTO.builder()
        .teamMemberId(teamMember.getTeamMemberId())
        .teamId(teamMember.getTeam().getTeamId())
        .memberId(teamMember.getMember().getMemberId())
        .memberRole(teamMember.getTeamMemberRole())
        .memberPosition(teamMember.getMemberPosition())
        .nickname(teamMember.getMember().getNickname())
        .build();
  }
}