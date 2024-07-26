package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.service.PostService.PostQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final PostQueryService postQueryService;

    //TODO: 로그인 구현 완료 시 principal 추가 필요
    @GetMapping("")
    public Map<String, List<String>> getRecentPosts( @RequestParam("year") String year, @RequestParam("month") String month) {
        try {
            int yearInt = Integer.parseInt(year);
            int monthInt = Integer.parseInt(month);

            YearMonth yearMonth = YearMonth.of(yearInt, monthInt);
            Long memberId = 1L;

            return postQueryService.getPostsByMonth(memberId, yearMonth);
        } catch (NumberFormatException e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }
}
