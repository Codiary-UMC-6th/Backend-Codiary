package com.codiary.backend.domain.alert.controller;

import com.codiary.backend.domain.alert.service.AlertService;
import com.codiary.backend.domain.member.security.CustomMemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
@Tag(name = "알람 API", description = "사용자 알람과 관련된 API를 제공합니다.")
public class AlertController {

    private final AlertService alertService;

    @GetMapping(path = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "알람")
    public SseEmitter alert(
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @PathParam("last_event") String lastEvent
    ) {
        Long memberId = memberDetails.getId();
        return alertService.connect(memberId, lastEvent);
    }

    @DeleteMapping("/disconnect")
    @Operation(summary = "알람 해지")
    public void disconnectAlert(
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        alertService.disconnect(memberId);
    }

}
