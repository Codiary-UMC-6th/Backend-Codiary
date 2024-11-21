package com.codiary.backend.domain.post.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PostRepositoryCustom {
    Page<Post> searchPost(String keyword, Pageable pageable);
    Map<Project, List<Post>> findPostsForCalendar(Long memberId, LocalDate date);
    Page<Post> findByBookmarkPostList(Member member, Pageable pageable);
}
