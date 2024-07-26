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

    public static PostResponseDTO.UpdatePostResultDTO toUpdatePostResultDTO(Post post) {
        return PostResponseDTO.UpdatePostResultDTO.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                //.postBody(post.getPostBody())
                .postStatus(post.getPostStatus())
                .postCategory(post.getPostCategory())
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .build();
    }

    public static PostResponseDTO.PostPreviewDTO toPostPreviewDTO(Post post) {
        return PostResponseDTO.PostPreviewDTO.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .postTitle(post.getPostTitle())
                .postStatus(post.getPostStatus())
                .postCategory(post.getPostCategory())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static PostResponseDTO.PostPreviewListDTO toPostPreviewListDTO(List<Post> postList) {
        List<PostResponseDTO.PostPreviewDTO> postPreviewDTOList = IntStream.range(0, postList.size())
                .mapToObj(i -> toPostPreviewDTO(postList.get(i)))
                .collect(Collectors.toList());
        return PostResponseDTO.PostPreviewListDTO.builder()
                .posts(postPreviewDTOList)
                .build();
    }

    public static PostResponseDTO.MemberPostResultDTO toMemberPostResultDTO(Post post) {
        return PostResponseDTO.MemberPostResultDTO.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postStatus(post.getPostStatus())
                .postCategory(post.getPostCategory())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static PostResponseDTO.MemberPostResultListDTO toMemberPostResultListDTO(List<Post> memberPostList) {
        List<PostResponseDTO.MemberPostResultDTO> memberPostResultDTOList = IntStream.range(0, memberPostList.size())
                .mapToObj(i -> toMemberPostResultDTO(memberPostList.get(i)))
                .collect(Collectors.toList());
        return PostResponseDTO.MemberPostResultListDTO.builder()
                .posts(memberPostResultDTOList)
                .build();
    }
}
