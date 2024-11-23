package com.codiary.backend.domain.post.repository;

import com.codiary.backend.domain.coauthor.entity.Author;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByPostAndMember(Post post, Member member);
}
