package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;

public class TeamConverter {
  public static TeamResponseDTO.CreateTeamResponseDTO toCreateMemberDTO(Team team) {
    return TeamResponseDTO.CreateTeamResponseDTO.builder()
        .teamId(team.getTeamId())
        .name(team.getName())
        .intro(team.getIntro())
        .profilePhoto(team.getProfilePhoto())
        .build();
  }
}
