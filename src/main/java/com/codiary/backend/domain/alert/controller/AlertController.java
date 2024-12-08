package com.codiary.backend.domain.alert.controller;

import com.codiary.backend.domain.alert.converter.AlertConverter;
import com.codiary.backend.domain.alert.dto.AlertResponseDTO;
import com.codiary.backend.domain.alert.entity.EventCategory;
import com.codiary.backend.domain.alert.entity.EventReceive;
import com.codiary.backend.domain.alert.service.AlertService;
import com.codiary.backend.domain.member.security.CustomMemberDetails;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            @RequestParam(value = "last_event", required = false) String lastEvent
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

    @PatchMapping("alert/on_off")
    @Operation(summary = "알림 on/off", description = "on/off 하고자 하는 알림 종류 입력시 해당 알림 on/off")
    public ApiResponse<AlertResponseDTO.AlertOnOffDTO> alertOnOff(
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @RequestParam(value = "event_category") EventCategory category
    ) {
        Long memberId = memberDetails.getId();
        EventReceive eventReceive = alertService.alertOnOff(category, memberId);
        return ApiResponse.onSuccess(SuccessStatus.ALERT_OK, AlertConverter.toAlertDTO(eventReceive));
    }
}
