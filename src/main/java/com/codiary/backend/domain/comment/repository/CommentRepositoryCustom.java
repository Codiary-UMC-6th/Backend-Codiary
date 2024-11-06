package com.codiary.backend.domain.comment.repository;

import com.codiary.backend.domain.comment.entity.Comment;
import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findByPostWithMemberInfoOrderByCreatedAtDesc(Long postId);
}
