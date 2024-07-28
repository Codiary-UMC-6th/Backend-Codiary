package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByPostTitleContainingIgnoreCaseOrderByCreatedAtDesc(String postTitle);
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByMember(Member member);

    @EntityGraph(attributePaths = {"project"})
    List<Post> findByMemberAndCreatedAtBetweenOrderByCreatedAtAsc(@Param("member") Member member, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
