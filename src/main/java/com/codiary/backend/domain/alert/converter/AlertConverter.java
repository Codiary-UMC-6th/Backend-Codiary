package com.codiary.backend.domain.alert.converter;

import com.codiary.backend.domain.alert.dto.AlertResponseDTO;
import com.codiary.backend.domain.alert.entity.EventReceive;

public class AlertConverter {

    public static AlertResponseDTO.AlertOnOffDTO toAlertDTO(EventReceive eventReceive) {
        return AlertResponseDTO.AlertOnOffDTO.builder()
                .memberId(eventReceive.getMember().getMemberId())
                .category(eventReceive.getEventCategory())
                .alertStatus(eventReceive.getStatus())
                .build();
    }
}
