package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByPostTitleContainingIgnoreCaseOrderByCreatedAtDesc(String postTitle);
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByMember(Member member);

    List<Post> findByMemberAndCreatedAtBetweenOrderByCreatedAtAsc(Member member, LocalDateTime startDate, LocalDateTime endDate);
}
