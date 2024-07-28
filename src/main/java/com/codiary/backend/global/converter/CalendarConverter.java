package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.web.dto.Calendar.CalendarDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarConverter {

    public static CalendarDTO.ToCalendarDTO toCalendarDTO(List<Post> postList) {
        Map<String, List<String[]>> activities = new HashMap<>();

        postList.forEach(post -> {
            String date = post.getCreatedAt().toLocalDate().toString();
            String projectName = post.getProject().getProjectName();
            String postTitle = post.getPostTitle();
            String[] activityDetail = {projectName, postTitle};
            activities.computeIfAbsent(date, k -> new ArrayList<>()).add(activityDetail);
        });

        return CalendarDTO.ToCalendarDTO.builder()
                .projectAndTitlesByDate(activities)
                .build();
    }

    public static CalendarDTO.ToProjectDTO toProjectsDTO(List<Post> postList) {
        Map<String, List<String>> activities = new HashMap<>();

        postList.forEach(post -> {
            String projectName = post.getProject().getProjectName();
            String postTitle = post.getPostTitle();
            activities.computeIfAbsent(projectName, k -> new ArrayList<>()).add(postTitle);
        });

        return CalendarDTO.ToProjectDTO.builder()
                .titlesByProject(activities)
                .build();
    }
}

