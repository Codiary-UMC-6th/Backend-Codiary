package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByPostTitleContainingIgnoreCaseOrderByCreatedAtDesc(String postTitle);
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByMember(Member member);

}
