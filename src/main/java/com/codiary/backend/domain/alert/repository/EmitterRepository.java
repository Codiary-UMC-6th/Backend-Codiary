package com.codiary.backend.domain.alert.repository;

import com.codiary.backend.domain.alert.entity.AlertEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {
    /*
    emitter key 값 member id 로 설정
     */
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, AlertEvent> events = new ConcurrentHashMap<>();

    public SseEmitter save(Long memberId, SseEmitter sseEmitter) {
        emitters.put(memberId, sseEmitter);
        return sseEmitter;
    }

    public AlertEvent save(AlertEvent event) {
        events.put(event.getEventId(), event);
        return event;
    }

    public Map<Long, SseEmitter> getEmittersByMembersId(List<Long> receiversId) {
        Map<Long, SseEmitter> emitterList = new HashMap<>();

        for (Long receiverId : receiversId) {
            SseEmitter emitter = emitters.get(receiverId);
            if (emitter != null) {
                emitterList.put(receiverId, emitter);
            }
        }

        return emitterList;
    }

    public SseEmitter getEmitterByMemberId(Long memberId) {
        return emitters.get(memberId);
    }

    public void deleteAllEmittersAboutMember(Long memberId) {
        emitters.remove(memberId);
    }

    public List<AlertEvent> getEventsAfterLastEvent(String lastEventId, Long receiverId) {
        List<AlertEvent> eventList = new ArrayList<>();

        events.forEach(
                (key, event) -> {
                    if (lastEventId.compareTo(key) < 0 && event.getAlertMemberIdList().contains(receiverId)) {
                        eventList.add(event);
                    }
                }
        );

        return eventList;
    }
}
