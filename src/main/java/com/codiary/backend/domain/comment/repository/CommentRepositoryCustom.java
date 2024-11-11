package com.codiary.backend.domain.comment.repository;

import com.codiary.backend.domain.comment.entity.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    List<Comment> findByPostWithMemberInfoAndRepliesOrderByCreatedAtAsc(Long postId, Pageable pageable);

    List<Comment> findByParentWithMemberInfoOrderByCreatedAtAsc(Long commentId, Pageable pageable);

    Optional<Comment> findByIdWithReplies(Long commentId);
}
