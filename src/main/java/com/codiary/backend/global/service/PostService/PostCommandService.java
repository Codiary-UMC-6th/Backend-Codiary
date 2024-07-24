package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;

public interface PostCommandService {

    // 포스트 생성
    Post createPost(Long memberId, Long teamId,PostRequestDTO.CreatePostRequestDTO request);
}
