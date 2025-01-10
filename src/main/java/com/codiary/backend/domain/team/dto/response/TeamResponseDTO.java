package com.codiary.backend.domain.team.dto.response;

import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TeamResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record SimpleTeamDTO(
            Long teamId,
            String teamName) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record TeamDTO(
            Long teamId,
            String name,
            String intro,
            String adminMail,
            String profileImageUrl,
            String bannerImageUrl,
            String github,
            String email,
            String linkedIn,
            String discord,
            String instagram) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record TeamProfileDTO(
            Long teamId,
            String name,
            String intro,
            String profileImageUrl,
            String bannerImageUrl,
            String github,
            String email,
            String linkedIn,
            String discord,
            String instagram,
            Boolean isFollowed,
            List<TeamMemberDTO> teamMemberList) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record TeamFollowDTO(
            Long teamFollowId,
            Long followerId,
            String followerName,
            Long followingTeamId,
            String followingTeamName,
            Boolean followStatus) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record TeamFollowersDTO(
        Long teamId,
        List<MemberResponseDTO.SimpleMemberDTO> followers) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record TeamMemberDTO(
            @Schema(description = "팀원 아이디", example = "1")
            Long teamMemberId,
            @Schema(description = "팀원 역할", example = "MEMBER | ADMIN")
            String teamMemberRole,
            @Schema(description = "팀원 포지션", example = "BACKEND | FRONTEND | DESIGNER | PLANNER")
            String teamMemberPosition,
            @Schema(description = "팀원 정보")
            MemberResponseDTO.SimpleMemberProfileDTO member) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record TeamImageDTO(
            String url) {
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record TeamPreviewDTO (
        Long teamId,
        String teamName
    ){}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record TeamPreviewListDTO (
        List<TeamResponseDTO.TeamPreviewDTO> teams
    ){}
}
