package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Comment;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Project;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.domain.entity.mapping.Categories;
import com.codiary.backend.global.repository.ProjectRepository;
import com.codiary.backend.global.repository.TeamRepository;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;
import com.codiary.backend.global.web.dto.Post.PostResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {

    public static Post toPost(PostRequestDTO.CreatePostRequestDTO request, TeamRepository teamRepository, ProjectRepository projectRepository) {
        Team team = null;
        Project project = null;
        if (request.getTeamId() != null) {
            team = teamRepository.findById(request.getTeamId()).orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + request.getTeamId()));
        }
        // projectId가 제공된 경우에만 Project 객체를 조회
        if (request.getProjectId() != null) {
            project = projectRepository.findById(request.getProjectId()).orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + request.getProjectId()));
        }
        return Post.builder()
                .postTitle(request.getPostTitle())
                .postBody(request.getPostBody())
                .team(team)
                .project(project)
                .postStatus(request.getPostStatus())
                .postAccess(request.getPostAccess())
                .build();
    }


    public static PostResponseDTO.CreatePostResultDTO toCreateResultDTO(Post post) {
        return PostResponseDTO.CreatePostResultDTO.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .projectId(post.getProject() != null ? post.getProject().getProjectId() : null)
                .postTitle(post.getPostTitle())
                .postBody(post.getPostBody())
                .postStatus(post.getPostStatus())
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .postAccess(post.getPostAccess())
                .thumbnailImageUrl((post.getThumbnailImage() != null)
                        ? post.getThumbnailImage().getFileUrl()
                        : "")
                .postFileList(PostFileConverter.toPostFileListDTO(post.getPostFileList()))
                .build();
    }

    public static PostResponseDTO.UpdatePostResultDTO toUpdatePostResultDTO(Post post) {
        return PostResponseDTO.UpdatePostResultDTO.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .projectId(post.getProject() != null ? post.getProject().getProjectId() : null)
                .postTitle(post.getPostTitle())
                .postBody(post.getPostBody())
                .postStatus(post.getPostStatus())
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .postAccess(post.getPostAccess())
                .thumbnailImageUrl((post.getThumbnailImage() != null)
                        ? post.getThumbnailImage().getFileUrl()
                        : "")
                .postFileList(PostFileConverter.toPostFileListDTO(post.getPostFileList()))
                .build();
    }

    // Post 조회
    public static PostResponseDTO.PostPreviewDTO toPostPreviewDTO(Post post) {
        List<String> postCategories = post.getCategoriesList().stream()
                .map(Categories::getName)
                .collect(Collectors.toList());

        return PostResponseDTO.PostPreviewDTO.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .projectId(post.getProject() != null ? post.getProject().getProjectId() : null)
                .postTitle(post.getPostTitle())
                .postBody(post.getPostBody())
                .postStatus(post.getPostStatus())
                .postCategory(String.join(", ", postCategories))
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .postAccess(post.getPostAccess())
                .thumbnailImageUrl((post.getThumbnailImage() != null)
                        ? post.getThumbnailImage().getFileUrl()
                        : "")
                .postFileList(PostFileConverter.toPostFileListDTO(post.getPostFileList()))
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // Post 전체 리스트 조회
    public static PostResponseDTO.PostPreviewListDTO toPostPreviewListDTO(Page<Post> posts) {
        List<PostResponseDTO.PostPreviewDTO> postPreviewDTOList = posts.getContent().stream()
                .map(PostConverter::toPostPreviewDTO)
                .collect(Collectors.toList());

        return PostResponseDTO.PostPreviewListDTO.builder()
                .posts(postPreviewDTOList)
                .listSize(posts.getNumberOfElements())
                .totalPage(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .isFirst(posts.isFirst())
                .isLast(posts.isLast())
                .build();
    }

    // 저자별 Post 조회
    public static PostResponseDTO.MemberPostPreviewDTO toMemberPostPreviewDTO(Post post) {
        List<String> postCategories = post.getCategoriesList().stream()
                .map(Categories::getName)
                .collect(Collectors.toList());

        return PostResponseDTO.MemberPostPreviewDTO.builder()
                .memberId(post.getMember().getMemberId())
                .postId(post.getPostId())
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .projectId(post.getProject() != null ? post.getProject().getProjectId() : null)
                .postTitle(post.getPostTitle())
                .postBody(post.getPostBody())
                .postStatus(post.getPostStatus())
                .postCategory(String.join(", ", postCategories))
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .postAccess(post.getPostAccess())
                .thumbnailImageUrl((post.getThumbnailImage() != null)
                        ? post.getThumbnailImage().getFileUrl()
                        : "")
                .postFileList(PostFileConverter.toPostFileListDTO(post.getPostFileList()))
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // 저자별 Post 페이징 조회
    public static PostResponseDTO.MemberPostPreviewListDTO toMemberPostPreviewListDTO(Page<Post> posts) {
        List<PostResponseDTO.MemberPostPreviewDTO> memberPostPreviewDTOList = posts.getContent().stream()
                .map(PostConverter::toMemberPostPreviewDTO)
                .collect(Collectors.toList());

        return PostResponseDTO.MemberPostPreviewListDTO.builder()
                .posts(memberPostPreviewDTOList)
                .listSize(posts.getNumberOfElements())
                .totalPage(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .isFirst(posts.isFirst())
                .isLast(posts.isLast())
                .build();
    }

    // 팀별 Post 조회
    public static PostResponseDTO.TeamPostPreviewDTO toTeamPostPreviewDTO(Post post) {
        List<String> postCategories = post.getCategoriesList().stream()
                .map(Categories::getName)
                .collect(Collectors.toList());

        return PostResponseDTO.TeamPostPreviewDTO.builder()
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .projectId(post.getProject() != null ? post.getProject().getProjectId() : null)
                .postTitle(post.getPostTitle())
                .postBody(post.getPostBody())
                .postStatus(post.getPostStatus())
                .postCategory(String.join(", ", postCategories))
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .postAccess(post.getPostAccess())
                .thumbnailImageUrl((post.getThumbnailImage() != null)
                        ? post.getThumbnailImage().getFileUrl()
                        : "")
                .postFileList(PostFileConverter.toPostFileListDTO(post.getPostFileList()))
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
        List<String> postCategories = post.getCategoriesList().stream()
                .map(Categories::getName)
                .collect(Collectors.toList());

        return PostResponseDTO.MemberPostInProjectPreviewDTO.builder()
                .projectId(post.getProject() != null ? post.getProject().getProjectId() : null)
                .memberId(post.getMember().getMemberId())
                .postId(post.getPostId())
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .postTitle(post.getPostTitle())
                .postBody(post.getPostBody())
                .postStatus(post.getPostStatus())
                .postCategory(String.join(", ", postCategories))
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .postAccess(post.getPostAccess())
                .thumbnailImageUrl((post.getThumbnailImage() != null)
                        ? post.getThumbnailImage().getFileUrl()
                        : "")
                .postFileList(PostFileConverter.toPostFileListDTO(post.getPostFileList()))
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
        List<String> postCategories = post.getCategoriesList().stream()
                .map(Categories::getName)
                .collect(Collectors.toList());

        return PostResponseDTO.TeamPostInProjectPreviewDTO.builder()
                .projectId(post.getProject() != null ? post.getProject().getProjectId() : null)
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .memberId(post.getMember().getMemberId())
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postBody(post.getPostBody())
                .postStatus(post.getPostStatus())
                .postCategory(String.join(", ", postCategories))
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .postAccess(post.getPostAccess())
                .thumbnailImageUrl((post.getThumbnailImage() != null)
                        ? post.getThumbnailImage().getFileUrl()
                        : "")
                .postFileList(PostFileConverter.toPostFileListDTO(post.getPostFileList()))
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
        List<String> postCategories = post.getCategoriesList().stream()
                .map(Categories::getName)
                .collect(Collectors.toList());

        return PostResponseDTO.MemberPostInTeamPreviewDTO.builder()
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .memberId(post.getMember().getMemberId())
                .postId(post.getPostId())
                .projectId(post.getProject() != null ? post.getProject().getProjectId() : null)
                .postTitle(post.getPostTitle())
                .postBody(post.getPostBody())
                .postStatus(post.getPostStatus())
                .postCategory(String.join(", ", postCategories))
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .postAccess(post.getPostAccess())
                .thumbnailImageUrl((post.getThumbnailImage() != null)
                        ? post.getThumbnailImage().getFileUrl()
                        : "")
                .postFileList(PostFileConverter.toPostFileListDTO(post.getPostFileList()))
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

    // 특정 Post의 인접한 Post 조회 (이전, 다음 Post 조회)
    public static PostResponseDTO.PostAdjacentDTO.PostAdjacentPreviewDTO toPostAdjacentPreviewDTO(Post post) {
        if (post == null) return null;

        List<String> postCategories = post.getCategoriesList().stream()
                .map(Categories::getName)
                .collect(Collectors.toList());

        return PostResponseDTO.PostAdjacentDTO.PostAdjacentPreviewDTO.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .projectId(post.getProject() != null ? post.getProject().getProjectId() : null)
                .postTitle(post.getPostTitle())
                .postBody(post.getPostBody())
                .postStatus(post.getPostStatus())
                .postCategory(String.join(", ", postCategories))
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .postAccess(post.getPostAccess())
                .thumbnailImageUrl((post.getThumbnailImage() != null)
                        ? post.getThumbnailImage().getFileUrl()
                        : "")
                .postFileList(PostFileConverter.toPostFileListDTO(post.getPostFileList()))
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static PostResponseDTO.PostAdjacentDTO toPostAdjacentDTO(Post.PostAdjacent adjacent) {
        return PostResponseDTO.PostAdjacentDTO.builder()
                .hadOlder(adjacent.getOlderPost() != null)
                .hasLater(adjacent.getLaterPost() != null)
                .olderPost(toPostAdjacentPreviewDTO(adjacent.getOlderPost()))
                .laterPost(toPostAdjacentPreviewDTO(adjacent.getLaterPost()))
                .build();
    }


    public static PostResponseDTO.UpdatePostResultDTO toSetPostCategoriesResultDTO(Post post) {
        List<String> postCategories = post.getCategoriesList().stream()
                .map(Categories::getName)
                .collect(Collectors.toList());

        return PostResponseDTO.UpdatePostResultDTO.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .teamId(post.getTeam() != null ? post.getTeam().getTeamId() : null)
                .projectId(post.getProject() != null ? post.getProject().getProjectId() : null)
                .postTitle(post.getPostTitle())
                .postBody(post.getPostBody())
                .postStatus(post.getPostStatus())
                .postCategory(String.join(", ", postCategories))
                .coauthorIds(post.getAuthorsList().stream()
                        .map(author -> author.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .postAccess(post.getPostAccess())
                .thumbnailImageUrl((post.getThumbnailImage() != null)
                        ? post.getThumbnailImage().getFileUrl()
                        : "")
                .postFileList(PostFileConverter.toPostFileListDTO(post.getPostFileList()))
                .build();
    }

    // 게시글에 댓글 작성하기
    public static Comment toComment(PostRequestDTO.CommentDTO request) {
        return Comment.builder()
                .commentBody(request.getCommentBody())
                .build();
    }

    public static PostResponseDTO.CreateCommentResultDTO toCreateCommentResultDTO(Comment comment) {
        return PostResponseDTO.CreateCommentResultDTO.builder()
                .commentId(comment.getCommentId())
                .memberId(comment.getMember().getMemberId())
                .postId(comment.getPost().getPostId())
                .nickname(comment.getMember().getNickname())
                .commentBody(comment.getCommentBody())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 게시글에 대댓글 작성하기
    public static Comment toCommentReply(PostRequestDTO.CommentReplyDTO request) {
        return Comment.builder()
                .commentBody(request.getCommentReplyBody())
                .build();
    }

    public static PostResponseDTO.CreateCommentReplyResultDTO toCreateCommentReplyResultDTO(Comment comment) {
        return PostResponseDTO.CreateCommentReplyResultDTO.builder()
                .commentId(comment.getCommentId())
                .memberId(comment.getMember().getMemberId())
                .postId(comment.getPost().getPostId())
                .parentId(comment.getParentId().getCommentId())
                .nickname(comment.getMember().getNickname())
                .commentBody(comment.getCommentBody())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 게시글별 댓글 조회
    public static PostResponseDTO.CommentDTO toCommentDTO(Comment comment) {
        return PostResponseDTO.CommentDTO.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .memberId(comment.getMember().getMemberId())
                .nickname(comment.getMember().getNickname())
                .commentBody(comment.getCommentBody())
                .createdAt(comment.getCreatedAt())
                .childCommentList(toCommentListDTO(comment.getChildComments()))
                .build();
    }

    public static List<PostResponseDTO.CommentDTO> toCommentListDTO(List<Comment> commentList) {
        return commentList.stream()
                .map(PostConverter::toCommentDTO)
                .collect(Collectors.toList());
    }

    // 메인페이지 인기글 전체 리스트 조회
    public static PostResponseDTO.PostPopularDTO toPostPopularDTO(Post post) {
        return PostResponseDTO.PostPopularDTO.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .fileUrl((post.getThumbnailImage() != null)
                        ? post.getThumbnailImage().getFileUrl()
                        : "")
                .postTitle(post.getPostTitle())
                .nickname(post.getMember().getNickname())
                .postBody(post.getPostBody())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static PostResponseDTO.PostPopularListDTO toPostPopularListDTO(Page<Post> postPopularList) {
        List<PostResponseDTO.PostPopularDTO> postPopularDTOList = postPopularList.stream()
                .map(PostConverter::toPostPopularDTO).collect(Collectors.toList());

        return PostResponseDTO.PostPopularListDTO.builder()
                .isLast(postPopularList.isLast())
                .isFirst(postPopularList.isFirst())
                .totalPage(postPopularList.getTotalPages())
                .totalElements(postPopularList.getTotalElements())
                .listSize(postPopularDTOList.size())
                .postPopularList(postPopularDTOList)
                .build();
    }

    // 메인페이지 인기글 멤버 관심 카테고리별 리스트 조회
    public static PostResponseDTO.PostPopularMemberCategoryDTO toPostPopularMemberCategoryDTO(Post post) {
        return PostResponseDTO.PostPopularMemberCategoryDTO.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .fileUrl((post.getThumbnailImage() != null)
                        ? post.getThumbnailImage().getFileUrl()
                        : "")
                .postTitle(post.getPostTitle())
                .nickname(post.getMember().getNickname())
                .postBody(post.getPostBody())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static PostResponseDTO.PostPopularMemberCategoryListDTO toPostPopularMemberCategoryListDTO(Page<Post> postPopularList) {
        List<PostResponseDTO.PostPopularMemberCategoryDTO> postPopularDTOList = postPopularList.stream()
                .map(PostConverter::toPostPopularMemberCategoryDTO).collect(Collectors.toList());

        return PostResponseDTO.PostPopularMemberCategoryListDTO.builder()
                .isLast(postPopularList.isLast())
                .isFirst(postPopularList.isFirst())
                .totalPage(postPopularList.getTotalPages())
                .totalElements(postPopularList.getTotalElements())
                .listSize(postPopularDTOList.size())
                .postPopularMemberCategoryList(postPopularDTOList)
                .build();
    }

    // 메인페이지 최신글 리스트 조회
    public static PostResponseDTO.PostLatestDTO toPostLatestDTO(Post post) {
        return PostResponseDTO.PostLatestDTO.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .fileUrl((post.getThumbnailImage() != null)
                        ? post.getThumbnailImage().getFileUrl()
                        : "")
                .postTitle(post.getPostTitle())
                .nickname(post.getMember().getNickname())
                .postBody(post.getPostBody())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static PostResponseDTO.PostLatestListDTO toPostLatestListDTO(Page<Post> postLatestList) {
        List<PostResponseDTO.PostLatestDTO> postLatestDTOList = postLatestList.stream()
                .map(PostConverter::toPostLatestDTO).collect(Collectors.toList());

        return PostResponseDTO.PostLatestListDTO.builder()
                .isLast(postLatestList.isLast())
                .isFirst(postLatestList.isFirst())
                .totalPage(postLatestList.getTotalPages())
                .totalElements(postLatestList.getTotalElements())
                .listSize(postLatestDTOList.size())
                .postLatestList(postLatestDTOList)
                .build();
    }

    // 메인페이지 팔로잉 게시글 리스트 조회
    public static PostResponseDTO.PostFollowingDTO toPostFollowingDTO(Post post) {
        return PostResponseDTO.PostFollowingDTO.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .fileUrl((post.getThumbnailImage() != null)
                        ? post.getThumbnailImage().getFileUrl()
                        : "")
                .postTitle(post.getPostTitle())
                .nickname(post.getMember().getNickname())
                .postBody(post.getPostBody())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static PostResponseDTO.PostFollowingListDTO toPostFollowingListDTO(Page<Post> postFollowingList) {
        List<PostResponseDTO.PostFollowingDTO> postFollowingDTOList = postFollowingList.stream()
                .map(PostConverter::toPostFollowingDTO).collect(Collectors.toList());

        return PostResponseDTO.PostFollowingListDTO.builder()
                .isLast(postFollowingList.isLast())
                .isFirst(postFollowingList.isFirst())
                .totalPage(postFollowingList.getTotalPages())
                .totalElements(postFollowingList.getTotalElements())
                .listSize(postFollowingDTOList.size())
                .postFollowingList(postFollowingDTOList)
                .build();
    }

}
