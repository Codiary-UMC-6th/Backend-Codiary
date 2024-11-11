package com.codiary.backend.domain.member.converter;

import com.codiary.backend.domain.member.dto.request.MemberRequestDTO;
import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.codiary.backend.domain.member.entity.Follow;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.dto.response.PostResponseDTO;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.project.dto.response.ProjectResponseDTO;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.team.dto.response.TeamResponseDTO;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.techstack.entity.TechStacks;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MemberConverter {
    public static MemberResponseDTO.SimpleMemberDTO toSimpleMemberResponseDto(Member member, Member user) {
        return MemberResponseDTO.SimpleMemberDTO.builder()
                .currentMemberId(member.getMemberId())
                .userId(user.getMemberId())
                .userName(user.getNickname())
                .photoUrl(member.getImage() != null ? member.getImage().getImageUrl() : "")
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

    public static MemberResponseDTO.SimpleMemberProfileDTO tosimpleMemberProfileResponseDto(Member member) {
        return MemberResponseDTO.SimpleMemberProfileDTO.builder()
                .userId(member.getMemberId())
                .userName(member.getNickname())
                .photoUrl(member.getImage() != null ? member.getImage().getImageUrl() : "")
                .build();
    }

    public static List<MemberResponseDTO.SimpleMemberProfileDTO> toSimpleMemberProfileListResponseDto(List<Member> members) {
        return members.stream()
                .map(member -> MemberResponseDTO.SimpleMemberProfileDTO.builder()
                        .userId(member.getMemberId())
                        .userName(member.getNickname())
                        .photoUrl(member.getImage() != null ? member.getImage().getImageUrl() : "")
                        .build())
                .collect(Collectors.toList());
    }

    public static MemberResponseDTO.MemberTechStackDTO toMemberTechStackResponseDto(Member updatedMember) {
        return MemberResponseDTO.MemberTechStackDTO.builder()
                .memberId(updatedMember.getMemberId())
                .techStacks(updatedMember.getTechStackList().stream()
                        .map(TechStacks::getName)
                        .collect(Collectors.toList()))
                .build();
    }

    // 회원가입시 활용
    public static Member toMember(MemberRequestDTO.MemberSignUpRequestDTO request, String password) {
        Member member = Member.builder()
                .email(request.email())
                .password(password)
                .nickname(request.nickname())
                .birth(request.birth().toString())
                .github(request.github())
                .linkedin(request.linkedin())
                .discord(request.discord())
                .build();
        return member;
    }

    public static MemberResponseDTO.MonthCalendarDTO toMonthCalendarResponseDto(Map<LocalDate, List<Project>> projects) {
        Map<String, List<ProjectResponseDTO.SimpleProjectResponseDTO>> projectMap = new HashMap<>();
        projects.forEach((date, projectList) -> {
            List<ProjectResponseDTO.SimpleProjectResponseDTO> projectResponses = projectList.stream()
                    .filter(Objects::nonNull)
                    .map(project -> ProjectResponseDTO.SimpleProjectResponseDTO.builder()
                            .projectId(project.getProjectId())
                            .name(project.getProjectName())
                            .build())
                    .toList();
            if (!projectResponses.isEmpty()) {
                projectMap.put(date.toString(), projectResponses);
            }
        });
        return MemberResponseDTO.MonthCalendarDTO.builder()
                .projectsByDate(projectMap)
                .build();
    }

    public static MemberResponseDTO.DayCalendarDTO toDayCalendarResponseDto(Map<Project, List<Post>> posts){
        Map<String, List<PostResponseDTO.PostTitleResponseDTO>> postMap = new HashMap<>();
        posts.forEach((project, postList) -> {
            List<PostResponseDTO.PostTitleResponseDTO> postResponses = postList.stream()
                    .filter(Objects::nonNull)
                    .map(post -> PostResponseDTO.PostTitleResponseDTO.builder()
                            .id(post.getPostId())
                            .title(post.getPostTitle())
                            .build())
                    .toList();
            if (!postResponses.isEmpty()) {
                postMap.put(project.getProjectName(), postResponses);
            }
        });
        return MemberResponseDTO.DayCalendarDTO.builder()
                .postsByDate(postMap)
                .build();
    }
}
