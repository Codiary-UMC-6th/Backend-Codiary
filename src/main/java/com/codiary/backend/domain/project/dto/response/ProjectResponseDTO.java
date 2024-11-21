package com.codiary.backend.domain.project.dto.response;

import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.util.List;

public class ProjectResponseDTO {

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record SimpleProjectResponseDTO(
            Long projectId,
            String name
    ) {
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ProjectDetailResponseDTO(
            Long projectId,
            String name,
            Boolean isTeam,
            List<MemberResponseDTO.SimpleMemberProfileDTO> projectMembers
    ) {
    }
}
