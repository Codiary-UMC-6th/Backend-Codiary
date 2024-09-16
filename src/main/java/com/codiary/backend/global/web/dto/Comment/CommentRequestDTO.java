package com.codiary.backend.global.web.dto.Comment;

import lombok.Builder;


public class CommentRequestDTO {

    //댓글 수정하기 요청 DTO
    public record PatchCommentDTO(String commentBody) {}

}
