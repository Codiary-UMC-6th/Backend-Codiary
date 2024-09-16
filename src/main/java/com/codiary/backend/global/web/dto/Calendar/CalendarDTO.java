package com.codiary.backend.global.web.dto.Calendar;

import lombok.Builder;

import java.util.List;
import java.util.Map;

public class CalendarDTO {

    // 날짜별 프로젝트와 제목 리스트 응답 DTO
    @Builder
    public record ToCalendarDTO(Map<String, List<String[]>> projectAndTitlesByDate) {}

    // 프로젝트별 제목 리스트 응답 DTO
    @Builder
    public record ToProjectDTO(Map<String, List<String>> titlesByProject) {}
}
