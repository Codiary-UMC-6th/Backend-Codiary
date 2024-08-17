package com.codiary.backend.global.web.dto.Comment;

import com.codiary.backend.global.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CommentResponseDTO {

    // 댓글 수정하기
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatchCommentResultDTO {
        Long commentId;
        Long memberId;
        Long postId;
        String nickname;
        String commentBody;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;

        // nickname 관련 could not initialize proxy - no Session 에러
        // DTO 변환을 Controller가 아닌 Service에서 하도록 추가
        // 트랜잭션 영역이 되기 때문에 DTO가 정상적으로 생성된다
        public PatchCommentResultDTO(Comment comment) {
            this.commentId = comment.getCommentId();
            this.memberId = comment.getMember().getMemberId();
            this.postId = comment.getPost().getPostId();
            this.nickname = comment.getMember().getNickname();
            this.commentBody = comment.getCommentBody();
            this.createdAt = comment.getCreatedAt();
            this.updatedAt = comment.getUpdatedAt();
        }
    }

    // 댓글 삭제하기
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteCommentResultDTO {
        Long commentId;
    }

    // 게시글의 댓글 개수 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountCommentsResultDTO {
        Long postId;
        int countComments;
    }

}
