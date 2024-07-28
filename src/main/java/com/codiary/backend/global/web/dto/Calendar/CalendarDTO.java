package com.codiary.backend.global.web.dto.Calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

public class CalendarDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToCalendarDTO {
        private Map<String, List<String[]>> projectAndTitlesByDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToProjectDTO {
        private Map<String, List<String>> titlesByProject;
    }
}
