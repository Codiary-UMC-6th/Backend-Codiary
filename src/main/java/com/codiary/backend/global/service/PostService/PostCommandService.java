package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;

public interface PostCommandService {

    // 포스트 생성
    Post createPost(Long memberId, Long teamId,PostRequestDTO.CreatePostRequestDTO request);
    //포스트 수정
    Post updatePost(Long memberId, Long postId, PostRequestDTO.UpdatePostDTO request);
    //포스트 삭제
    void deletePost(Long memberId, Long postId);
    // 공개/비공개 설정
    Post updateVisibility(Long postId, PostRequestDTO.UpdateVisibilityRequestDTO request);
    // 공동 저자 설정
    Post updateCoauthors(Long postId, PostRequestDTO.UpdateCoauthorRequestDTO request);
}
