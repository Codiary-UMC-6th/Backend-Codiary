package com.codiary.backend.domain.member.dto.response;

import com.codiary.backend.domain.team.dto.response.TeamResponseDTO;
import com.codiary.backend.domain.techstack.enumerate.TechStack;
import com.codiary.backend.global.jwt.TokenInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberResponseDTO {

    @Builder
    public record MemberTokenResponseDTO(
            TokenInfo tokenInfo,
            String email,
            String nickname,
            Long memberId) {}

    @Builder
    public record MemberImageDTO(String url) {}

    @Builder
    public record SimpleMemberDTO(
            Long currentMemberId,
            Long userId,
            String userName,
            String photoUrl,
            String githubUrl,
            String linkedinUrl,
            String discordUrl,
            String introduction,
            List<TechStack> techStacksList,
            List<TeamResponseDTO.SimpleTeamDTO> teamList,
            Boolean myPage) {}

    @Builder
    public record FollowDTO(
            Long followId,
            Long followerId,
            String followerName,
            Long followingId,
            String followingName,
            Boolean followStatus) {}
}
