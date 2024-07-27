package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;
import com.codiary.backend.global.web.dto.Post.PostResponseDTO;
import lombok.Builder;
import org.springframework.data.domain.Page;

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
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .postAccess(post.getPostAccess())
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
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .postAccess(post.getPostAccess())
                .build();
    }

    // Post 조회
    public static PostResponseDTO.PostPreviewDTO toPostPreviewDTO(Post post) {
        return PostResponseDTO.PostPreviewDTO.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .postTitle(post.getPostTitle())
                .postStatus(post.getPostStatus())
                .postCategory(post.getPostCategory())
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .postAccess(post.getPostAccess())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // Post 전체 리스트 조회
    public static PostResponseDTO.PostPreviewListDTO toPostPreviewListDTO(List<Post> postList) {
        List<PostResponseDTO.PostPreviewDTO> postPreviewDTOList = IntStream.range(0, postList.size())
                .mapToObj(i -> toPostPreviewDTO(postList.get(i)))
                .collect(Collectors.toList());
        return PostResponseDTO.PostPreviewListDTO.builder()
                .posts(postPreviewDTOList)
                .build();
    }

    // 저자별 Post 조회
    public static PostResponseDTO.MemberPostResultDTO toMemberPostResultDTO(Post post) {
        return PostResponseDTO.MemberPostResultDTO.builder()
                .memberId(post.getMember().getMemberId())
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postStatus(post.getPostStatus())
                .postCategory(post.getPostCategory())
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .postAccess(post.getPostAccess())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
    // 저자별 Post 리스트 조회
    public static PostResponseDTO.MemberPostResultListDTO toMemberPostResultListDTO(List<Post> memberPostList) {
        List<PostResponseDTO.MemberPostResultDTO> memberPostResultListDTO = IntStream.range(0, memberPostList.size())
                .mapToObj(i -> toMemberPostResultDTO(memberPostList.get(i)))
                .collect(Collectors.toList());
        return PostResponseDTO.MemberPostResultListDTO.builder()
                .posts(memberPostResultListDTO)
                .build();
    }

    // 팀별 Post 조회
    public static PostResponseDTO.TeamPostPreviewDTO toTeamPostPreviewDTO(Post post) {
        return PostResponseDTO.TeamPostPreviewDTO.builder()
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postStatus(post.getPostStatus())
                .postCategory(post.getPostCategory())
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .postAccess(post.getPostAccess())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // 팀별 Post 페이징 조회
    public static PostResponseDTO.TeamPostPreviewListDTO toTeamPostPreviewListDTO(Page<Post> posts) {
        List<PostResponseDTO.TeamPostPreviewDTO> teamPostPreviewDTOList = posts.getContent().stream()
                .map(PostConverter::toTeamPostPreviewDTO)
                .collect(Collectors.toList());

        return PostResponseDTO.TeamPostPreviewListDTO.builder()
                .posts(teamPostPreviewDTOList)
                .listSize(posts.getNumberOfElements())
                .totalPage(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .isFirst(posts.isFirst())
                .isLast(posts.isLast())
                .build();
    }


    // 프로젝트별 저자의 Post 조회
    public static PostResponseDTO.MemberPostInProjectPreviewDTO toMemberPostInProjectPreviewDTO(Post post) {
        return PostResponseDTO.MemberPostInProjectPreviewDTO.builder()
                .projectId(post.getProject().getProjectId())
                .memberId(post.getMember().getMemberId())
                .postId(post.getPostId())
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .postTitle(post.getPostTitle())
                .postStatus(post.getPostStatus())
                .postCategory(post.getPostCategory())
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .postAccess(post.getPostAccess())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // 프로젝트별 저자의 Post 페이징 조회
    public static PostResponseDTO.MemberPostInProjectPreviewListDTO toMemberPostInProjectPreviewListDTO(Page<Post> posts) {
        List<PostResponseDTO.MemberPostInProjectPreviewDTO> memberPostInProjectPreviewListDTO = posts.getContent().stream()
                .map(PostConverter::toMemberPostInProjectPreviewDTO)
                .collect(Collectors.toList());

        return PostResponseDTO.MemberPostInProjectPreviewListDTO.builder()
                .posts(memberPostInProjectPreviewListDTO)
                .listSize(posts.getNumberOfElements())
                .totalPage(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .isFirst(posts.isFirst())
                .isLast(posts.isLast())
                .build();
    }

    // 프로젝트별 팀의 Post 조회
    public static PostResponseDTO.TeamPostInProjectPreviewDTO toTeamPostInProjectPreviewDTO(Post post) {
        return PostResponseDTO.TeamPostInProjectPreviewDTO.builder()
                .projectId(post.getProject().getProjectId())
                .teamId(post.getTeam().getTeamId())
                .postId(post.getPostId())
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .postTitle(post.getPostTitle())
                .postStatus(post.getPostStatus())
                .postCategory(post.getPostCategory())
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .postAccess(post.getPostAccess())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // 프로젝트별 팀의 Post 페이징 조회
    public static PostResponseDTO.TeamPostInProjectPreviewListDTO toTeamPostInProjectPreviewListDTO(Page<Post> posts) {
        List<PostResponseDTO.TeamPostInProjectPreviewDTO> teamPostInProjectPreviewListDTO = posts.getContent().stream()
                .map(PostConverter::toTeamPostInProjectPreviewDTO)
                .collect(Collectors.toList());

        return PostResponseDTO.TeamPostInProjectPreviewListDTO.builder()
                .posts(teamPostInProjectPreviewListDTO)
                .listSize(posts.getNumberOfElements())
                .totalPage(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .isFirst(posts.isFirst())
                .isLast(posts.isLast())
                .build();
    }

    // 팀별 멤버의 Post 조회
    public static PostResponseDTO.MemberPostInTeamPreviewDTO toMemberPostInTeamPreviewDTO(Post post) {
        return PostResponseDTO.MemberPostInTeamPreviewDTO.builder()
                .teamId(post.getTeam().getTeamId())
                .memberId(post.getMember().getMemberId())
                .postId(post.getPostId())
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .postTitle(post.getPostTitle())
                .postStatus(post.getPostStatus())
                .postCategory(post.getPostCategory())
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .postAccess(post.getPostAccess())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // 팀별 멤버의 Post 페이징 조회
    public static PostResponseDTO.MemberPostInTeamPreviewListDTO toMemberPostInTeamPreviewListDTO(Page<Post> posts) {
        List<PostResponseDTO.MemberPostInTeamPreviewDTO> memberPostInTeamPreviewListDTO = posts.getContent().stream()
                .map(PostConverter::toMemberPostInTeamPreviewDTO)
                .collect(Collectors.toList());

        return PostResponseDTO.MemberPostInTeamPreviewListDTO.builder()
                .posts(memberPostInTeamPreviewListDTO)
                .listSize(posts.getNumberOfElements())
                .totalPage(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .isFirst(posts.isFirst())
                .isLast(posts.isLast())
                .build();
    }

}
