package com.codiary.backend.domain.post.converter;

import com.codiary.backend.domain.post.dto.response.PostResponseDTO;
import com.codiary.backend.domain.post.entity.Post;
import org.springframework.data.domain.Page;

public class PostConverter {
    public static Page<PostResponseDTO.SimplePostResponseDTO> toPostListResponseDto(Page<Post> postList) {
        return postList.map(PostConverter::toSimplePostResponseDto);
    }

    private static PostResponseDTO.SimplePostResponseDTO toSimplePostResponseDto(Post post) {
        return PostResponseDTO.SimplePostResponseDTO.builder()
                .id(post.getPostId())
                .title(post.getPostTitle())
                .body(post.getPostBody())
                .author(post.getMember() != null ? post.getMember().getNickname() : null)
                .createdAt(post.getCreatedAt())
                .thumbnailImage(post.getThumbnailImage() != null ? post.getThumbnailImage().getFileUrl() : null)
                .build();
    }
}
