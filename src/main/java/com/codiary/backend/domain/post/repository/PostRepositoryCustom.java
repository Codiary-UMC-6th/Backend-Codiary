package com.codiary.backend.domain.post.repository;

import com.codiary.backend.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> searchPost(Long memberId, String keyword, Pageable pageable);

    Page<Post> getLatestPostsOfFollowings(Long memberId, Pageable pageable);

    Page<Post> getLatestPosts(Pageable pageable);

    Page<Post> getPopularPosts(Pageable pageable);

    Page<Post> getPopularPostsByCategoryId(Long memberId, Long categoryId, Pageable pageable);
}
