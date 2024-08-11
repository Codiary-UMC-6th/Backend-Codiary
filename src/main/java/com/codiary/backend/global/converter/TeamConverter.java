package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.domain.entity.TeamFollow;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;
import com.codiary.backend.global.web.dto.TeamMember.TeamMemberResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TeamConverter {
  //팀생성
  public static TeamResponseDTO.CreateTeamResponseDTO toCreateMemberDTO(Team team) {
    return TeamResponseDTO.CreateTeamResponseDTO.builder()
        .teamId(team.getTeamId())
        .name(team.getName())
        .intro(team.getIntro())
        .bannerImageUrl((team.getBannerImage() != null)
            ? team.getBannerImage().getImageUrl()
            : "")
        .profileImageUrl((team.getProfileImage() != null)
            ? team.getProfileImage().getImageUrl()
            : "")
        .build();
  }

  //팀 조회
  public static TeamResponseDTO.TeamCheckResponseDTO toTeamCheckResponseDTO(Team team) {
    List<TeamMemberResponseDTO.TeamMemberDTO> members = team.getTeamMemberList().stream()
        .map(TeamMemberConverter::toTeamMemberDTO)
        .collect(Collectors.toList());

    return TeamResponseDTO.TeamCheckResponseDTO.builder()
        .teamId(team.getTeamId())
        .name(team.getName())
        .intro(team.getIntro())
        .bannerImageUrl((team.getBannerImage() != null)
            ? team.getBannerImage().getImageUrl()
            : "")
        .profileImageUrl((team.getProfileImage() != null)
            ? team.getProfileImage().getImageUrl()
            : "")
        .github(team.getGithub())
        .email(team.getEmail())
        .linkedIn(team.getLinkedin())
        .members(members)
        .build();
  }

  // 팀 프로필 수정
  public static TeamResponseDTO.UpdateTeamDTO toUpdateTeamDTO(Team team) {
    return TeamResponseDTO.UpdateTeamDTO.builder()
        .name(team.getName())
        .intro(team.getIntro())
        .bannerImageUrl((team.getBannerImage() != null)
            ? team.getBannerImage().getImageUrl()
            : "")
        .profileImageUrl((team.getProfileImage() != null)
            ? team.getProfileImage().getImageUrl()
            : "")
        .github(team.getGithub())
        .linkedIn(team.getLinkedin())
        .discord(team.getDiscord())
        .instagram(team.getInstagram())
        .build();
  }

  //팀 팔로우
  public static TeamResponseDTO.TeamFollowResponseDto toTeamFollowResponseDto(TeamFollow teamFollow) {
    return TeamResponseDTO.TeamFollowResponseDto.builder()
        .followId(teamFollow.getTeamFollowId())
        .memberId(teamFollow.getMember().getMemberId())
        .memberName(teamFollow.getMember().getNickname())
        .teamId(teamFollow.getTeam().getTeamId())
        .teamName(teamFollow.getTeam().getName())
        .followStatus(teamFollow.getFollowStatus())
        .build();
  }
}