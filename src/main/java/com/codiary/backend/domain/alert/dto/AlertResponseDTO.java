package com.codiary.backend.domain.alert.dto;

import com.codiary.backend.domain.alert.entity.EventCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

public class AlertResponseDTO {

    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record AlertOnOffDTO(
            Long memberId,
            EventCategory category,
            Boolean alertStatus
    ) {
    }
}
