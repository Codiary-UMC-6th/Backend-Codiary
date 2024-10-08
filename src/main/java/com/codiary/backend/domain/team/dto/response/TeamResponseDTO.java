package com.codiary.backend.domain.team.dto.response;

import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TeamResponseDTO {

    @Builder
    public record SimpleTeamDTO(
            Long teamId,
            String teamName) {}

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
            List<MemberResponseDTO.SimpleMemberProfileDTO> teamMemberList){}
}
