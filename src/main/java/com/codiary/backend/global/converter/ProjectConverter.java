package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Project;
import com.codiary.backend.global.domain.entity.mapping.Categories;
import com.codiary.backend.global.web.dto.Category.CategoryResponseDTO;
import com.codiary.backend.global.web.dto.Project.ProjectResponseDTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProjectConverter {

    public static ProjectResponseDTO.ProjectPreviewDTO toProjectPreviewDTO(Project project) {
        return ProjectResponseDTO.ProjectPreviewDTO.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .build();
    }

    public static ProjectResponseDTO.ProjectPreviewListDTO toProjectPreviewListDTO(List<Project> projectList) {
        List<ProjectResponseDTO.ProjectPreviewDTO> projectPreviewDTOList = IntStream.range(0, projectList.size())
                .mapToObj(i->toProjectPreviewDTO(projectList.get(i)))
                .collect(Collectors.toList());
        return ProjectResponseDTO.ProjectPreviewListDTO.builder()
                .projects(projectPreviewDTOList)
                .build();
    }
}
