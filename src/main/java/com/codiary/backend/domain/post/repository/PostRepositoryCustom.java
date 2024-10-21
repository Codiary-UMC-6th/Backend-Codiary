package com.codiary.backend.domain.post.repository;

import com.codiary.backend.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> searchPost(String keyword, Pageable pageable);
}
