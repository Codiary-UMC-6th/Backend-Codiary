package com.codiary.backend.global.service.BookmarkService;

import com.codiary.backend.global.domain.entity.Bookmark;

public interface BookmarkCommandService {

    // 북마크 추가
    Bookmark createBookmark(Long memberId, Long postId);

    // 북마크 삭제
    void deleteBookmark(Long bookmarkId);

    // 게시글의 북마크 개수 조회
    int countBookmark(Long postId);

}
