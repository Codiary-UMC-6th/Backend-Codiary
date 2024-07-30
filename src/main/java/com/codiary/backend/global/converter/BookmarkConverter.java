package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Bookmark;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.web.dto.Bookmark.BookmarkResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookmarkConverter {

    // 북마크 추가
    // Controller
    public static BookmarkResponseDTO.CreateBookmarkResultDTO toCreateBookmarkResultDTO(Bookmark bookmark) {
        return BookmarkResponseDTO.CreateBookmarkResultDTO.builder()
                .bookmarkId(bookmark.getId())
                .memberId(bookmark.getMember().getMemberId())
                .postId(bookmark.getPost().getPostId())
                .createdAt(LocalDateTime.now())
                .build();
    }
    // Service
    public static Bookmark toBookmark(Member member, Post post) {
        return Bookmark.builder()
                .member(member)
                .post(post)
                .build();
    }

    // 북마크 삭제
    public static BookmarkResponseDTO.DeleteBookmarkResultDTO toDeleteBookmarkResultDTO(Long bookmarkId) {
        return BookmarkResponseDTO.DeleteBookmarkResultDTO.builder()
                .bookmarkId(bookmarkId)
                .build();
    }

    // 게시글의 북마크 개수 조회
    public static BookmarkResponseDTO.CountBookmarkResultDTO toCountBookmarkResultDTO(Long postId, int countBookmark) {
        return BookmarkResponseDTO.CountBookmarkResultDTO.builder()
                .postId(postId)
                .countBookmark(countBookmark)
                .build();
    }

}
