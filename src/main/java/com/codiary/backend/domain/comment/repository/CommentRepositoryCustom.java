package com.codiary.backend.domain.comment.repository;

import com.codiary.backend.domain.comment.entity.Comment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    Page<Comment> findByPostWithMemberInfoAndRepliesOrderByCreatedAtAsc(Long postId, Pageable pageable);

    Page<Comment> findByParentWithMemberInfoOrderByCreatedAtAsc(Long commentId, Pageable pageable);

    Optional<Comment> findByIdWithReplies(Long commentId);
}
