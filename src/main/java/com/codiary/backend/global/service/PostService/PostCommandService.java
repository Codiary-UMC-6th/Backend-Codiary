package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;

import java.util.Set;

public interface PostCommandService {

    // 포스트 생성
    Post createPost(PostRequestDTO.CreatePostRequestDTO request);
    //포스트 수정
    Post updatePost(Long postId, PostRequestDTO.UpdatePostDTO request);
    //포스트 삭제
    void deletePost(Long memberId, Long postId);
    // 공개/비공개 설정
    Post updateVisibility(Long postId, Long memberId, PostRequestDTO.UpdateVisibilityRequestDTO request);
    // 공동 저자 설정
    Post updateCoauthors(Long postId, Long memberId, PostRequestDTO.UpdateCoauthorRequestDTO request);
    // 글 소속 팀 설정
    Post setPostTeam(Long postId, Long memberId, Long teamId);

    Post setPostCategories(Long postId, Set<String> categories);

}
