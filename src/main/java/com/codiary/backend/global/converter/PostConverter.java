package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;
import com.codiary.backend.global.web.dto.Post.PostResponseDTO;
import lombok.Builder;

public class PostConverter {

    public static Post toPost(PostRequestDTO.CreatePostRequestDTO request){
        return Post.builder()
                .postTitle(request.getPostTitle())
                .postBody(request.getPostBody())
                .postStatus(request.getPostStatus())
                .postCategory(request.getPostCategory())
                .build();
    }

    public static PostResponseDTO.CreatePostResultDTO toCreateResultDTO(Post post){
        return PostResponseDTO.CreatePostResultDTO.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postBody(post.getPostBody())
                .postStatus(post.getPostStatus())
                .postCategory(post.getPostCategory())
                .build();
    }
}
