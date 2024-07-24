package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;
import com.codiary.backend.global.web.dto.Post.PostResponseDTO;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PostConverter {

    public static Post toPost(PostRequestDTO.CreatePostRequestDTO request) {
        return Post.builder()
                .postTitle(request.getPostTitle())
                .postBody(request.getPostBody())
                .postStatus(request.getPostStatus())
                .postCategory(request.getPostCategory())
                .postAccess(request.getPostAccess())
                .build();
    }

    public static PostResponseDTO.CreatePostResultDTO toCreateResultDTO(Post post) {
        return PostResponseDTO.CreatePostResultDTO.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                //.postBody(post.getPostBody())
                .postStatus(post.getPostStatus())
                .postCategory(post.getPostCategory())
                .build();
    }

    public static PostResponseDTO.UpdatePostResultDTO UpdatePostResultDTO(Post post) {
        return PostResponseDTO.UpdatePostResultDTO.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                //.postBody(post.getPostBody())
                .postStatus(post.getPostStatus())
                .postCategory(post.getPostCategory())
                .build();
    }


    public static PostResponseDTO.PostPreviewDTO toPostPreviewDTO(Post post) {
        return PostResponseDTO.PostPreviewDTO.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postStatus(post.getPostStatus())
                .postCategory(post.getPostCategory())
                .build();
    }

    public static PostResponseDTO.PostPreviewListDTO toPostPreviewListDTO(
            List<Post> postList
    ){
        List<PostResponseDTO.PostPreviewDTO> postPreviewDTOList = IntStream.range(0, postList.size())
                .mapToObj(i-> toPostPreviewDTO(postList.get(i)))
                .collect(Collectors.toList());
        return PostResponseDTO.PostPreviewListDTO.builder()
                .posts(postPreviewDTOList)
                .build();
    }
}
