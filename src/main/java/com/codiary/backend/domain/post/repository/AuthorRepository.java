package com.codiary.backend.domain.post.repository;

import com.codiary.backend.domain.coauthor.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
