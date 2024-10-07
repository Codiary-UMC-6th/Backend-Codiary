package com.codiary.backend.domain.team.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TeamRequestDTO {
    public record CreateTeamDTO(
            String name,
            String intro,
            String github,
            String email,
            String linkedIn,
            String discord,
            String instagram
    ) {}

    public record UpdateTeamDTO(
            String name,
            String intro,
            String github,
            String linkedIn,
            String discord,
            String instagram
    ) {}
}
