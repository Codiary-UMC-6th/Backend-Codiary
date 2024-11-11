package com.codiary.backend.domain.comment.repository;

import com.codiary.backend.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    List<Comment> findByPostWithMemberInfoAndRepliesOrderByCreatedAtAsc(Long postId, Pageable pageable);

    List<Comment> findByParentWithMemberInfoOrderByCreatedAtAsc(Long commentId, Pageable pageable);
}
