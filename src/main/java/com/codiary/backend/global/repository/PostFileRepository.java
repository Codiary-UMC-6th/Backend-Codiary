package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {
}
