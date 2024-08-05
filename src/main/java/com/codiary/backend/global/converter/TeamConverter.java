package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;

public class TeamConverter {
  //팀생성
  public static TeamResponseDTO.CreateTeamResponseDTO toCreateMemberDTO(Team team) {
    return TeamResponseDTO.CreateTeamResponseDTO.builder()
        .teamId(team.getTeamId())
        .name(team.getName())
        .intro(team.getIntro())
        .profilePhoto(team.getProfilePhoto())
        .build();
  }

  //팀 조회
  public static TeamResponseDTO.TeamCheckResponseDTO toTeamCheckDTO(Team team) {
    return TeamResponseDTO.TeamCheckResponseDTO.builder()
        .name(team.getName())
        .intro(team.getIntro())
        .profilePhoto(team.getProfilePhoto())
        .github(team.getGithub())
        .email(team.getEmail())
        .linkedIn(team.getLinkedin())
        .build();
  }

  // 팀 프로필 수정
  public static TeamResponseDTO.UpdateTeamDTO toUpdateTeamDTO(Team team) {
    return TeamResponseDTO.UpdateTeamDTO.builder()
        .name(team.getName())
        .intro(team.getIntro())
        .profilePhoto(team.getProfilePhoto())
        .github(team.getGithub())
        .linkedIn(team.getLinkedin())
        .discord(team.getDiscord())
        .instagram(team.getInstagram())
        .build();
  }
}
