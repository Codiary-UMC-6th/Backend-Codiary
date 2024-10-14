package com.codiary.backend.domain.team.converter;

import com.codiary.backend.domain.member.converter.MemberConverter;
import com.codiary.backend.domain.team.dto.response.TeamResponseDTO;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.entity.TeamFollow;
import com.codiary.backend.domain.team.entity.TeamMember;

import java.util.List;
import java.util.stream.Collectors;

public class TeamConverter {
    public static TeamResponseDTO.TeamDTO toTeamResponseDto(Team team) {
        return TeamResponseDTO.TeamDTO.builder()
                .teamId(team.getTeamId())
                .name(team.getName())
                .intro(team.getIntro())
                .adminMail(team.getEmail())
                .profileImageUrl(team.getProfileImage() == null ? null : team.getProfileImage().getImageUrl())
                .bannerImageUrl(team.getBannerImage() == null ? null : team.getBannerImage().getImageUrl())
                .github(team.getGithub())
                .email(team.getEmail())
                .linkedIn(team.getLinkedin())
                .discord(team.getDiscord())
                .instagram(team.getInstagram())
                .build();
    }

    //팀 팔로우 여부 구현 후 수정 필요
    public static TeamResponseDTO.TeamProfileDTO toTeamProfileResponseDto(Team team){
        return TeamResponseDTO.TeamProfileDTO.builder()
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
                .isFollowed(false)
                .teamMemberList(team.getTeamMemberList() == null ? null :
                        MemberConverter.toSimpleMemberProfileResponseDto(team.getTeamMemberList()
                                .stream()
                                .map(TeamMember::getMember)
                                .collect(Collectors.toList())))
                .build();
    }

    public static TeamResponseDTO.TeamFollowDTO toTeamFollowResponseDTO(TeamFollow teamFollow) {
        return TeamResponseDTO.TeamFollowDTO.builder()
                .teamFollowId(teamFollow.getTeamFollowId())
                .followerId(teamFollow.getMember().getMemberId())
                .followerName(teamFollow.getMember().getNickname())
                .followingTeamId(teamFollow.getTeam().getTeamId())
                .followingTeamName(teamFollow.getTeam().getName())
                .followStatus(teamFollow.getFollowStatus())
                .build();
    }

    public static TeamResponseDTO.TeamFollowersDTO toTeamFollowersResponseDTO(Long teamId, List<TeamFollow> followers) {
        return TeamResponseDTO.TeamFollowersDTO.builder()
                .teamId(teamId)
                .followers(MemberConverter.toSimpleFollowResponseDto(
                                followers.stream()
                                        .map(TeamFollow::getMember)
                                        .collect(Collectors.toList())
                        )
                )
                .build();
    }
}
