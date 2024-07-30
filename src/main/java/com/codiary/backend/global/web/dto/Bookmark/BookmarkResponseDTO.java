package com.codiary.backend.global.web.dto.Bookmark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BookmarkResponseDTO {

    // 북마크 추가
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBookmarkResultDTO {
        Long bookmarkId;
        Long memberId;
        Long postId;
        LocalDateTime createdAt;
    }

    // 북마크 삭제
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteBookmarkResultDTO {
        Long bookmarkId;
    }

    // 게시글의 북마크 개수 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountBookmarkResultDTO {
        Long postId;
        int countBookmark;
    }

}
