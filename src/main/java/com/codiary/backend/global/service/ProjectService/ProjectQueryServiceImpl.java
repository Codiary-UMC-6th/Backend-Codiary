package com.codiary.backend.global.service.ProjectService;

import com.codiary.backend.global.domain.entity.Project;
import com.codiary.backend.global.repository.ProjectRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@Getter
public class ProjectQueryServiceImpl implements ProjectQueryService{

    private final ProjectRepository projectRepository;

    @Override
    public List<Project> getProjects() {
        return projectRepository.findAllByOrderByProjectIdDesc();
    }
}
