package com.codiary.backend.domain.comment.converter;

import com.codiary.backend.domain.comment.dto.response.CommentResponseDTO;
import com.codiary.backend.domain.comment.entity.Comment;
import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {

    public static CommentResponseDTO.CommentDTO toCommentResponseDto(Comment comment) {
        return CommentResponseDTO.CommentDTO.builder()
                .commentId(comment.getCommentId())
                .commentBody(comment.getCommentBody())
                .postId((comment.getPost() != null) ? comment.getPost().getPostId() : null)
                .parentId((comment.getParent() != null) ? comment.getParent().getCommentId() : null)
                .commenterId(comment.getMember().getMemberId())
                .commenterProfileImageUrl(
                        (comment.getMember().getImage() != null) ? (comment.getMember().getImage().getImageUrl()) : "")
                .commenterNickname(comment.getMember().getNickname())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .numberOfReply((comment.getParent() == null) ? comment.getChildComments().size() : null)
                .build();
    }

    public static List<CommentResponseDTO.CommentDTO> toCommentResponseListDto(List<Comment> comments) {
        return comments.stream()
                .map(CommentConverter::toCommentResponseDto)
                .collect(Collectors.toList());
    }
}
