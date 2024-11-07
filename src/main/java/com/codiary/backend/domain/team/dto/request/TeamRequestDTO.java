package com.codiary.backend.domain.team.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

public class TeamRequestDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record CreateTeamDTO(
            String name,
            String intro,
            String github,
            String linkedIn,
            String discord,
            String instagram
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record UpdateTeamDTO(
            String name,
            String email,
            String intro,
            String github,
            String linkedIn,
            String discord,
            String instagram
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record TeamMemberDTO(
            @Schema(description = "추가할 팀원 닉네임", example = "abc123*")
            String memberNickName,
            @Schema(description = "추가할 팀원 역할", example = "MEMBER | ADMIN")
            String memberRole
    ){}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record TeamImageDTO(
            MultipartFile image
    ) {
    }
}
