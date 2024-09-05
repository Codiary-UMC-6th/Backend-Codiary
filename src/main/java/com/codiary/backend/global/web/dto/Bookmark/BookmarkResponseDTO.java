package com.codiary.backend.global.web.dto.Bookmark;

import lombok.Builder;
import java.time.LocalDateTime;

public class BookmarkResponseDTO {

    // 북마크 추가 응답 DTO
    @Builder
    public record CreateBookmarkResultDTO(Long bookmarkId, Long memberId, Long postId, LocalDateTime createdAt) {}

    // 북마크 삭제 응답 DTO
    @Builder
    public record DeleteBookmarkResultDTO(Long bookmarkId) {}

    // 게시글의 북마크 개수 조회 응답 DTO
    @Builder
    public record CountBookmarkResultDTO(Long postId, int countBookmark) {}
}
