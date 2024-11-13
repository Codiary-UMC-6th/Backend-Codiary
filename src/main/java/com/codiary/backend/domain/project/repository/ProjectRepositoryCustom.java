package com.codiary.backend.domain.project.repository;

import com.codiary.backend.domain.project.entity.Project;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ProjectRepositoryCustom {
    Map<LocalDate, List<Project>> findProjectsForCalendar(Long memberId, LocalDate startDate, LocalDate endDate);
}
