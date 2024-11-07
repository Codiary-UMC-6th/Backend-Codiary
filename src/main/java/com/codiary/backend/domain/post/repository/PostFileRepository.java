package com.codiary.backend.domain.post.repository;

import com.codiary.backend.domain.post.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {
}
