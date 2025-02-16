package com.codiary.backend.domain.team.converter;

import com.codiary.backend.domain.member.converter.MemberConverter;
import com.codiary.backend.domain.project.converter.ProjectConverter;
import com.codiary.backend.domain.project.dto.response.ProjectResponseDTO;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.team.dto.response.TeamResponseDTO;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.entity.TeamBannerImage;
import com.codiary.backend.domain.team.entity.TeamFollow;
import com.codiary.backend.domain.team.entity.TeamMember;
import com.codiary.backend.domain.team.entity.TeamProfileImage;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codiary.backend.domain.team.entity.QTeamMember.teamMember;

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
                        TeamConverter.toTeamMemberListResponseDTO(team))
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

    public static TeamResponseDTO.TeamMemberDTO toTeamMemberResponseDTO(TeamMember teamMember){
        return TeamResponseDTO.TeamMemberDTO.builder()
                .teamMemberId(teamMember.getTeamMemberId())
                .member(MemberConverter.toSimpleMemberProfileResponseDto(teamMember.getMember()))
                .teamMemberRole(teamMember.getTeamMemberRole().name())
                .teamMemberPosition(teamMember.getMemberPosition())
                .build();

    }

    public static List<TeamResponseDTO.TeamMemberDTO> toTeamMemberListResponseDTO(Team team) {
        return team.getTeamMemberList().stream()
                .map(TeamConverter::toTeamMemberResponseDTO)
                .collect(Collectors.toList());
    }

    public static TeamResponseDTO.TeamImageDTO toTeamImageResponseDTO(TeamProfileImage profileImage) {
        return TeamResponseDTO.TeamImageDTO.builder()
                .url(profileImage.getImageUrl())
                .build();
    }

    public static TeamResponseDTO.TeamImageDTO toTeamImageResponseDTO(TeamBannerImage bannerImage) {
        return TeamResponseDTO.TeamImageDTO.builder()
                .url(bannerImage.getImageUrl())
                .build();
    }

    public static TeamResponseDTO.TeamPreviewDTO toTeamPreviewDTO(Team team) {
        return TeamResponseDTO.TeamPreviewDTO.builder()
                .teamId(team.getTeamId())
                .teamName(team.getName())
                .build();
    }

    public static TeamResponseDTO.TeamPreviewListDTO toTeamPreviewListDTO(List<Team> teamList) {
        List<TeamResponseDTO.TeamPreviewDTO> teamPreviewDTOList = IntStream.range(0, teamList.size())
                .mapToObj(i->toTeamPreviewDTO(teamList.get(i)))
                .collect(Collectors.toList());
        return TeamResponseDTO.TeamPreviewListDTO.builder()
                .teams(teamPreviewDTOList)
                .build();
    }

    public static TeamResponseDTO.SimpleTeamDTO toSimpleTeamResponseDTO(TeamMember teamMember) {
        return TeamResponseDTO.SimpleTeamDTO.builder()
                .teamId(teamMember.getTeam().getTeamId())
                .teamName(teamMember.getTeam().getName())
                .build();
    }

    public static List<TeamResponseDTO.SimpleTeamDTO> toSimpleTeamListResponseDTO(List<TeamMember> teams) {
        return teams.stream()
                .map(TeamConverter::toSimpleTeamResponseDTO)
                .collect(Collectors.toList());
    }



}
