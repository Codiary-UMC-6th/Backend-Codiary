package com.codiary.backend.domain.alert.entity;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AlertEvent {
    private final EventCategory kindOfEvent;
    private final String eventId;
    private final List<Long> alertMemberIdList;
    private final Object data;

    @Builder
    public AlertEvent(EventCategory kindOfEvent, String eventId, Object data, List<Long> alertMemberIdList) {
        this.kindOfEvent = kindOfEvent;
        this.eventId = eventId;
        this.data = data;
        this.alertMemberIdList = alertMemberIdList;
    }
}
