package com.codiary.backend.domain.member.dto.response;

import com.codiary.backend.global.jwt.TokenInfo;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class MemberResponseDTO {

    @Builder
    public record MemberTokenResponseDTO(
            TokenInfo tokenInfo,
            String email,
            String nickname,
            Long memberId) {}

    @Builder
    public record BookmarkDTO(
            Long memberId,
            Long bookmarkId,
            Long postId,
            String thumbnailImageUrl,
            String postTitle,
            String nickname,
            String postBody,
            LocalDateTime createdAt) {}

    // 회원별 관심 카테고리탭 리스트 조회
    @Builder
    public record MemberCategoryListDTO(
            List<MemberCategoryDTO> memberCategoryList,
            Integer listSize) {}

    @Builder
    public record MemberCategoryDTO(
            Long memberId,
            Long memberCategoryId,
            Long categoryId,
            String categoryName,
            LocalDateTime createdAt) {}

    @Builder
    public record MemberImageDTO(String url) {}

    @Builder
    public record ProjectsDTO(
            Long memberId,
            List<String> projectList) {}
}
