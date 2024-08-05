package com.codiary.backend.global.web.dto.Member;

import com.codiary.backend.global.domain.enums.TechStack;
import com.codiary.backend.global.jwt.TokenInfo;
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

    // 회원별 관심 카테고리탭 리스트 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberCategoryListDTO {
        List<MemberResponseDTO.MemberCategoryDTO> memberCategoryList;
        Integer listSize;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberCategoryDTO {
        Long memberId;
        Long memberCategoryId;
        Long categoryId;
        String categoryName;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberImageDTO {
        String url;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserProfileDTO {
        Long currentMemberId;
        Long userId;
        String photoUrl;
        String githubUrl;
        String linkedinUrl;
        String discordUrl;
        String introduction;
        List<TechStack> techStacksList;
        List<String> teamList;
        Boolean myPage;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor // 기본 생성자 추가
    @Getter
    @Setter
    public static class TechStacksDTO {
        private Long memberId;
        private List<TechStack> techStackList;
    }
}
