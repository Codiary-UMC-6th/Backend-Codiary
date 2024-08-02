package com.codiary.backend.global.service.ProjectService;

import com.codiary.backend.global.domain.entity.Project;

import java.util.List;

public interface ProjectQueryService {
    List<Project> getProjects();
}
