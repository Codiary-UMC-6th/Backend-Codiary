package com.codiary.backend.domain.member.controller;

import com.codiary.backend.domain.member.converter.MemberConverter;
import com.codiary.backend.domain.member.dto.request.MemberRequestDTO;
import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.security.CustomMemberDetails;
import com.codiary.backend.domain.member.service.MemberCommandService;
import com.codiary.backend.domain.member.service.MemberQueryService;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.techstack.enumerate.TechStack;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/member")
@Tag(name = "회원 API", description = "회원정보 조회/수정/삭제 관련 API입니다.")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @PatchMapping(path = "/profile-image", consumes = "multipart/form-data")
    @Operation(summary = "프로필 사진 설정")
    public ApiResponse<MemberResponseDTO.MemberImageDTO> updateProfileImage(@AuthenticationPrincipal CustomMemberDetails memberDetails, @ModelAttribute MemberRequestDTO.MemberProfileImageRequestDTO request) {
        return memberCommandService.updateProfileImage(memberDetails.getId(), request);
    }

    @DeleteMapping("/profile-image")
    @Operation(summary = "프로필 사진 삭제")
    public ApiResponse<String> deleteProfileImage(@AuthenticationPrincipal CustomMemberDetails memberDetails) {
        return memberCommandService.deleteProfileImage(memberDetails.getId());
    }

    @GetMapping("/{member_id}/profile-image")
    @Operation(summary = "사용자 프로필 사진 조회")
    public ApiResponse<MemberResponseDTO.MemberImageDTO> getProfileImage(@PathVariable("member_id") Long memberId) {
        return memberQueryService.getProfileImage(memberId);
    }

    @GetMapping("/profile/{member_id}")
    @Operation(summary = "사용자 프로필 기본 정보 조회", description = "마이페이지 사용자 정보 조회 기능")
    public ApiResponse<MemberResponseDTO.SimpleMemberDTO> getUserProfile(@AuthenticationPrincipal CustomMemberDetails memberDetails, @PathVariable(value = "member_id") Long memberId){
        Member currentMember = memberQueryService.getUserInfo(memberDetails.getId());
        Member user = memberQueryService.getUserProfile(memberId);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toSimpleMemberResponseDto(currentMember, user));
    }

    @PutMapping("/info")
    @Operation(summary = "사용자 정보 수정", description = "마이페이지 사용자 정보 수정 기능")
    public ApiResponse<MemberResponseDTO.MemberInfoDTO> updateUserInfo(@AuthenticationPrincipal CustomMemberDetails memberDetails, @Valid @RequestBody MemberRequestDTO.MemberInfoDTO request){
        Member updatedMember = memberCommandService.updateMemberInfo(memberDetails.getId(), request);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toMemberInfoResponseDto(updatedMember));
    }

    @GetMapping("/info")
    @Operation(summary = "사용자 정보 조회", description = "마이페이지 사용자 정보 조회 기능")
    public ApiResponse<MemberResponseDTO.MemberInfoDTO> getUserInfo(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        Long memberId = customMemberDetails.getId();
        Member fetchedMember = memberQueryService.getUserInfo(memberId);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toMemberInfoResponseDto(fetchedMember));
    }

    @PatchMapping("/techstack/{techstack_name}")
    @Operation(summary = "사용자 기술스택 추가", description = "마이페이지 사용자 기술스택 추가 기능")
    public ApiResponse<MemberResponseDTO.MemberTechStackDTO> addTechStack(@AuthenticationPrincipal CustomMemberDetails memberDetails, @PathVariable(value = "techstack_name") TechStack techStackName){
        Member updatedMember = memberCommandService.addTechStack(memberDetails.getId(), techStackName);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toMemberTechStackResponseDto(updatedMember));
    }

    @GetMapping("/calendar/month")
    @Operation(summary = "월별 사용자 캘린더 조회", description = "마이페이지 사용자 월별 캘린더 조회 기능")
    public ApiResponse<MemberResponseDTO.MonthCalendarDTO> getCalendar(@AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                                                        @RequestParam("year") String year,
                                                                        @RequestParam("month") String month){
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        YearMonth yearMonth = YearMonth.of(yearInt, monthInt);
        Map<LocalDate, List<Project>> projects = memberQueryService.getProjectsByMonth(customMemberDetails.getId(), yearMonth);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toMonthCalendarResponseDto(projects));
    }

    @GetMapping("/calendar/day")
    @Operation(summary = "날짜 별 사용자 캘린더 조회", description = "프로젝트별 사용자 POST 조회 기능")
    public ApiResponse<MemberResponseDTO.DayCalendarDTO> getCalendar(@AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                                                        @RequestParam("date") LocalDate date){
        Long memberId = customMemberDetails.getId();
        Map<Project, List<Post>> postList = memberQueryService.getPostsByDay(memberId, date);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toDayCalendarResponseDto(postList));
    }


}
