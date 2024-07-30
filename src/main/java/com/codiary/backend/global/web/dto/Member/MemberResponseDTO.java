package com.codiary.backend.global.web.dto.Member;

import com.codiary.backend.global.jwt.TokenInfo;
import com.codiary.backend.global.web.dto.Bookmark.BookmarkResponseDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class MemberResponseDTO {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberTokenResponseDTO {
        TokenInfo tokenInfo;
        String email;
        String nickname;
    }



    // 회원별 북마크 리스트 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkListDTO {
        List<MemberResponseDTO.BookmarkDTO> bookmarkList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkDTO {
        Long memberId;
        Long bookmarkId;
        Long postId;
        String photoUrl;
        String postTitle;
        String nickname;
        String postBody;
        LocalDateTime createdAt;
    }

}
