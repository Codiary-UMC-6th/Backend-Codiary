package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
