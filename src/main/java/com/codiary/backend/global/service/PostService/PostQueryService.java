package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.domain.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostQueryService {

    List<Post> findAllBySearch(Optional<String> optSearch);
    List<Post> getMemberPost(Long memberId);
}
