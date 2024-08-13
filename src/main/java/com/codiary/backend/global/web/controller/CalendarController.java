package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.converter.CalendarConverter;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.service.MemberService.MemberCommandService;
import com.codiary.backend.global.service.PostService.PostQueryService;
import com.codiary.backend.global.web.dto.Calendar.CalendarDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final PostQueryService postQueryService;
    private final MemberCommandService memberCommandService;

    @Operation(
            summary = "해당 달의 캘린더 조회 API"
            , description = "날짜별로 프로젝트명 및 글 제목을 반환합니다. 조회하고자 하는 연도와 월을 입력해주세요."
            //, security = @SecurityRequirement(name = "accessToken")
    )
    @GetMapping("")
    public ApiResponse<CalendarDTO.ToCalendarDTO> getRecentPosts(@RequestParam("year") String year, @RequestParam("month") String month) {
        try {
            int yearInt = Integer.parseInt(year);
            int monthInt = Integer.parseInt(month);

            YearMonth yearMonth = YearMonth.of(yearInt, monthInt);
            Member member = memberCommandService.getRequester();

            List<Post> postList = postQueryService.getPostsByMonth(member, yearMonth);
            return ApiResponse.onSuccess(
                    SuccessStatus.POST_OK,
                    CalendarConverter.toCalendarDTO(postList)
            );
        } catch (NumberFormatException e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }

    @Operation(
            summary = "해당 달의 프로젝트 및 글 제목 조회 API"
            , description = "해당 달의 프로젝트 별 글 제목을 반환합니다. 조회하고자 하는 연도와 월을 입력해주세요."
            //, security = @SecurityRequirement(name = "accessToken")
    )
    @GetMapping("/project")
    public ApiResponse<CalendarDTO.ToProjectDTO> getRecentProjects(@RequestParam("year") String year, @RequestParam("month") String month) {
        try {
            int yearInt = Integer.parseInt(year);
            int monthInt = Integer.parseInt(month);

            YearMonth yearMonth = YearMonth.of(yearInt, monthInt);
            Member member = memberCommandService.getRequester();

            List<Post> postList = postQueryService.getPostsByMonth(member, yearMonth);
            return ApiResponse.onSuccess(
                    SuccessStatus.POST_OK,
                    CalendarConverter.toProjectsDTO(postList)
            );
        } catch (NumberFormatException e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }
}
