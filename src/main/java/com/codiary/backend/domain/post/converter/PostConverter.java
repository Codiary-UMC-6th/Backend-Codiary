package com.codiary.backend.domain.post.converter;

import com.codiary.backend.domain.post.dto.request.PostRequestDTO;
import com.codiary.backend.domain.post.dto.response.PostResponseDTO;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.team.entity.Team;
import org.springframework.data.domain.Page;
import com.codiary.backend.domain.project.repository.ProjectRepository;
import com.codiary.backend.domain.team.repository.TeamRepository;

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
        if (request.teamId() != null) {
            team = teamRepository.findById(request.teamId()).orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + request.teamId()));
        }
        if (request.projectId() != null) {
            project = projectRepository.findById(request.projectId()).orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + request.projectId()));
        }
        return Post.builder()
                .postTitle(request.postTitle())
                .postBody(request.postBody())
                .team(team)
                .project(project)
                .postStatus(request.postStatus() != null ? request.postStatus() : true)  // 기본값 설정
                .postAccess(request.postAccess() != null ? request.postAccess() : PostAccess.MEMBER)  // 기본값 설정
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




}
