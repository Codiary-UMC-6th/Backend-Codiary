package com.codiary.backend.domain.team.converter;

import com.codiary.backend.domain.team.dto.response.TeamResponseDTO;
import com.codiary.backend.domain.team.entity.Team;

public class TeamConverter {
    public static TeamResponseDTO.TeamDTO toTeamResponseDto(Team team) {
        return TeamResponseDTO.TeamDTO.builder()
                .teamId(team.getTeamId())
                .name(team.getName())
                .intro(team.getIntro())
                .profileImageUrl(team.getProfileImage() == null ? null : team.getProfileImage().getImageUrl())
                .bannerImageUrl(team.getBannerImage() == null ? null : team.getBannerImage().getImageUrl())
                .github(team.getGithub())
                .email(team.getEmail())
                .linkedIn(team.getLinkedin())
                .discord(team.getDiscord())
                .instagram(team.getInstagram())
                .build();
    }
}
