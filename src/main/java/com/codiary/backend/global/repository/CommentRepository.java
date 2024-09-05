package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Comment;
import com.codiary.backend.global.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글별 댓글 조회
    List<Comment> findAllByPostAndParentIdIsNull(Post post);

    // 게시글의 댓글 개수 조회
    int countByPost(Post post);

}
