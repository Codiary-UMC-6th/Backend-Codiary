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

    @PatchMapping("alert/bookmark")
    @Operation(summary = "북마크 알림 on/off", description = "다른 유저가 해당 유저의 게시물을 북마크 한 경우 전송되는 알림 on/off")
    public ApiResponse<AlertResponseDTO.AlertOnOffDTO> bookmarkAlert(
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        EventReceive eventReceive = alertService.alertOnOff(EventCategory.BOOKMARK, memberId);
        return ApiResponse.onSuccess(SuccessStatus.ALERT_OK, AlertConverter.toAlertDTO(eventReceive));
    }

    @PatchMapping("alert/comment")
    @Operation(summary = "댓글 알림 on/off", description = "다른 유저가 해당 유저의 게시물에 댓글 단 경우 전송되는 알림 on/off")
    public ApiResponse<AlertResponseDTO.AlertOnOffDTO> commentAlert(
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        EventReceive eventReceive = alertService.alertOnOff(EventCategory.COMMENT, memberId);
        return ApiResponse.onSuccess(SuccessStatus.ALERT_OK, AlertConverter.toAlertDTO(eventReceive));
    }

    @PatchMapping("alert/join_team")
    @Operation(summary = "팀 합류 알림 on/off", description = "유저가 팀에 합류한 경우 보내는 알림 on/off")
    public ApiResponse<AlertResponseDTO.AlertOnOffDTO> joinTeamAlert(
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        EventReceive eventReceive = alertService.alertOnOff(EventCategory.JOIN_TEAM, memberId);
        return ApiResponse.onSuccess(SuccessStatus.ALERT_OK, AlertConverter.toAlertDTO(eventReceive));
    }

    @PatchMapping("alert/kicked_out_team")
    @Operation(summary = "팀 추방 알림 on/off", description = "유저가 팀으로부터 추방 당한 경우 전송되는 알림 on/off")
    public ApiResponse<AlertResponseDTO.AlertOnOffDTO> kickedOutTeamAlert(
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        EventReceive eventReceive = alertService.alertOnOff(EventCategory.KICKED_OUT_TEAM, memberId);
        return ApiResponse.onSuccess(SuccessStatus.ALERT_OK, AlertConverter.toAlertDTO(eventReceive));
    }

    @PatchMapping("alert/team_follow")
    @Operation(summary = "팀 팔로우 알림 on/off", description = "다른 유저가 해당 유저가 관리자인 팀을 팔로우 한 경우 전송되는 알림 on/off")
    public ApiResponse<AlertResponseDTO.AlertOnOffDTO> teamFollowAlert(
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        EventReceive eventReceive = alertService.alertOnOff(EventCategory.TEAM_FOLLOW, memberId);
        return ApiResponse.onSuccess(SuccessStatus.ALERT_OK, AlertConverter.toAlertDTO(eventReceive));
    }

    @PatchMapping("alert/follow")
    @Operation(summary = "팔로우 알림 on/off", description = "다른 유저가 해당 유저를 팔로우 한 경우 전송되는 알림 on/off")
    public ApiResponse<AlertResponseDTO.AlertOnOffDTO> followAlert(
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        EventReceive eventReceive = alertService.alertOnOff(EventCategory.FOLLOW, memberId);
        return ApiResponse.onSuccess(SuccessStatus.ALERT_OK, AlertConverter.toAlertDTO(eventReceive));
    }

    @PatchMapping("alert/following_member_new_post")
    @Operation(summary = "팔로우 멤버 새 다이어리 알림 on/off", description = "해당 유저가 팔로우 하는 유저의 새 게시물 생성 시 전송되는 알림 on/off")
    public ApiResponse<AlertResponseDTO.AlertOnOffDTO> memberNewPostAlert(
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        EventReceive eventReceive = alertService.alertOnOff(EventCategory.FOLLOWING_MEMBER_NEW_POST, memberId);
        return ApiResponse.onSuccess(SuccessStatus.ALERT_OK, AlertConverter.toAlertDTO(eventReceive));
    }

    @PatchMapping("alert/following_team_new_post")
    @Operation(summary = "팔로우 팀 새 다이어리 알림 on/off", description = "해당 유저가 팔로우 하는 팀의 새 게시물 생성 시 전송되는 알림 on/off")
    public ApiResponse<AlertResponseDTO.AlertOnOffDTO> teamNewPostAlert(
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        EventReceive eventReceive = alertService.alertOnOff(EventCategory.FOLLOWING_TEAM_NEW_POST, memberId);
        return ApiResponse.onSuccess(SuccessStatus.ALERT_OK, AlertConverter.toAlertDTO(eventReceive));
    }

    @PatchMapping("alert/my_team_new_post")
    @Operation(summary = "소속된 팀의 새 다이어리 알림 on/off", description = "해당 유저가 소속된 팀의 새 게시물 생성 시 전송되는 알림 on/off")
    public ApiResponse<AlertResponseDTO.AlertOnOffDTO> myTeamNewPostAlert(
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        EventReceive eventReceive = alertService.alertOnOff(EventCategory.MY_TEAM_NEW_POST, memberId);
        return ApiResponse.onSuccess(SuccessStatus.ALERT_OK, AlertConverter.toAlertDTO(eventReceive));
    }
}
