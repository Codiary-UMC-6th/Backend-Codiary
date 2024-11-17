package com.codiary.backend.domain.post.repository;

import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PostRepositoryCustom {
    Page<Post> searchPost(Long memberId, String keyword, Pageable pageable);

    Page<Post> getLatestPostsOfFollowings(Long memberId, Pageable pageable);

    Page<Post> getLatestPosts(Pageable pageable);

    Page<Post> getPopularPosts(Pageable pageable);

    Page<Post> getPopularPostsByCategoryId(Long memberId, Long categoryId, Pageable pageable);
    
    Map<Project, List<Post>> findPostsForCalendar(Long memberId, LocalDate date);
}
