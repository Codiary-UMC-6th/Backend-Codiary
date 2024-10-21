package com.codiary.backend.domain.post.service;

import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Page<Post> searchPost(String keyword, Pageable pageable) {
        //business logic & return
        return postRepository.searchPost(keyword, pageable);
    }
}
