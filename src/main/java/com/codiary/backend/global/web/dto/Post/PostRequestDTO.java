package com.codiary.backend.global.web.dto.Post;

import com.codiary.backend.global.domain.enums.PostAccess;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostRequestDTO {

    // 게시글 생성 요청 DTO
    public record CreatePostRequestDTO(
            Long teamId,
            Long projectId,
            String postTitle,
            String postBody,
            Boolean postStatus,
            PostAccess postAccess,
            String thumbnailImageName,
            List<MultipartFile> postFiles) {}

    // 게시글 수정 요청 DTO
    public record UpdatePostDTO(
            String postTitle,
            String postBody,
            Boolean postStatus,
            PostAccess postAccess,
            String thumbnailImageName,
            List<MultipartFile> addedPostFiles) {}

    // 게시글 공개/비공개 상태 업데이트 요청 DTO
    public record UpdateVisibilityRequestDTO(Boolean postStatus) {}

    // 게시글 공동 저자 업데이트 요청 DTO
    public record UpdateCoauthorRequestDTO(List<Long> memberIds) {}

    // 게시글에 팀 설정 요청 DTO
    public record SetTeamRequestDTO(Long teamId) {}

    // 게시글에 댓글 작성 요청 DTO
    public record CommentDTO(@NotBlank String commentBody) {}

    // 게시글에 대댓글 작성 요청 DTO
    public record CommentReplyDTO(@NotBlank String commentReplyBody) {}
}
