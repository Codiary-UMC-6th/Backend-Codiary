package com.codiary.backend.domain.alert.repository;

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

    public SseEmitter save(Long memberId, SseEmitter sseEmitter) {
        emitters.put(memberId, sseEmitter);
        return sseEmitter;
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

    public void deleteAllEmittersAboutMember(Long memberId) {
        emitters.remove(memberId);
    }
}
