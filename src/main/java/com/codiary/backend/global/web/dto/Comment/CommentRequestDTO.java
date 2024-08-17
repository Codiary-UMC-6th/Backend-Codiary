package com.codiary.backend.global.web.dto.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentRequestDTO {

    // 댓글 수정하기
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatchCommentDTO {
        String commentBody;
    }

}
