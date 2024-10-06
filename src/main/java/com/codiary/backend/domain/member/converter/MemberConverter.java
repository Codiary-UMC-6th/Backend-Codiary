package com.codiary.backend.domain.member.converter;

import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.codiary.backend.domain.member.entity.Follow;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.team.dto.response.TeamResponseDTO;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.techstack.entity.TechStacks;

import java.util.List;
import java.util.stream.Collectors;

public class MemberConverter {
    public static MemberResponseDTO.SimpleMemberDTO toSimpleMemberResponseDto(Member member, Member user) {
        return MemberResponseDTO.SimpleMemberDTO.builder()
                .currentMemberId(member.getMemberId())
                .userId(user.getMemberId())
                .userName(user.getNickname())
                .photoUrl((member.getImage() != null)
                        ? member.getImage().getImageUrl()
                        : "")
                .githubUrl(user.getGithub())
                .linkedinUrl(user.getLinkedin())
                .discordUrl(user.getDiscord())
                .introduction(user.getIntroduction())
                .techStacksList(user.getTechStackList().stream()
                        .map(TechStacks::getName)
                        .collect(Collectors.toList()))
                .teamList(user.getTeamMemberList().stream()
                        .map(teamMember -> {
                            Team team = teamMember.getTeam();
                            return TeamResponseDTO.SimpleTeamDTO.builder()
                                    .teamId(team.getTeamId())
                                    .teamName(team.getName())
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .myPage(user.getMemberId().equals(member.getMemberId()))
                .build();
    }

    public static MemberResponseDTO.MemberInfoDTO toMemberInfoResponseDto(Member member) {
        return MemberResponseDTO.MemberInfoDTO.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .birth(member.getBirth())
                .introduction(member.getIntroduction())
                .github(member.getGithub())
                .linkedin(member.getLinkedin())
                .discord(member.getDiscord())
                .build();
    }

    public static MemberResponseDTO.FollowDTO toFollowDto(Follow follow) {
        return MemberResponseDTO.FollowDTO.builder()
                .followId(follow.getFollowId())
                .followerId(follow.getFromMember().getMemberId())
                .followerName(follow.getFromMember().getNickname())
                .followingId(follow.getToMember().getMemberId())
                .followingName(follow.getToMember().getNickname())
                .followStatus(follow.getFollowStatus())
                .build();
    }

    public static List<MemberResponseDTO.SimpleMemberDTO> toSimpleFollowResponseDto(List<Member> members) {
        return members.stream()
                .map(member -> MemberResponseDTO.SimpleMemberDTO.builder()
                        .userId(member.getMemberId())
                        .userName(member.getNickname())
                        .photoUrl((member.getImage() != null)
                                ? member.getImage().getImageUrl()
                                : "")
                        .build())
                .collect(Collectors.toList());
    }
}
