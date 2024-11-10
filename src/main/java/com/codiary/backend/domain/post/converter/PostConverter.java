package com.codiary.backend.domain.post.converter;

import com.codiary.backend.domain.post.dto.request.PostRequestDTO;
import com.codiary.backend.domain.post.dto.response.PostResponseDTO;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.category.entity.Category;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.team.entity.Team;
import org.springframework.data.domain.Page;
import com.codiary.backend.domain.project.repository.ProjectRepository;
import com.codiary.backend.domain.team.repository.TeamRepository;

import java.util.List;
import java.util.stream.Collectors;

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


    public static Post toPost(PostRequestDTO.CreatePostRequestDTO request, TeamRepository teamRepository, ProjectRepository projectRepository) {
        Team team = null;
        Project project = null;
        if (request.getTeamId() != null) {
            team = teamRepository.findById(request.getTeamId()).orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + request.getTeamId()));
        }
        if (request.getProjectId() != null) {
            project = projectRepository.findById(request.getProjectId()).orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + request.getProjectId()));
        }
        return Post.builder()
                .postTitle(request.getPostTitle())
                .postBody(request.getPostBody())
                .team(team)
                .project(project)
                .postStatus(request.getPostStatus() != null ? request.getPostStatus() : true)  // 기본값 설정
                .postAccess(request.getPostAccess() != null ? request.getPostAccess() : PostAccess.MEMBER)  // 기본값 설정
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


    // 저자별 Post 조회
    public static PostResponseDTO.MemberPostPreviewDTO toMemberPostPreviewDTO(Post post) {
        List<String> postCategories = post.getCategoriesList().stream()
                .map(Category::getName)
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
                .map(Category::getName)
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



}
