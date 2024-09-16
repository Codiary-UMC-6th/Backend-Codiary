package com.codiary.backend.global.web.dto.Member;

import com.codiary.backend.global.domain.enums.TechStack;
import com.codiary.backend.global.jwt.TokenInfo;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;
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

    // 회원별 북마크 리스트 조회
    @Builder
    public record BookmarkListDTO(
            List<BookmarkDTO> bookmarkList,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            Boolean isFirst,
            Boolean isLast) {}

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
    public record UserProfileDTO(
            Long currentMemberId,
            Long userId,
            String userName,
            String photoUrl,
            String githubUrl,
            String linkedinUrl,
            String discordUrl,
            String introduction,
            List<TechStack> techStacksList,
            List<TeamResponseDTO.TeamPreviewDTO> teamList,
            Boolean myPage) {}

    @Builder
    public record UserInfoDTO(
            Long memberId,
            String email,
            String nickname,
            String birth,
            String introduction,
            String githubUrl,
            String linkedinUrl,
            String discordUrl) {}

    @Builder
    public record TechStacksDTO(
            Long memberId,
            List<TechStack> techStackList) {}

    @Builder
    public record ProjectsDTO(
            Long memberId,
            List<String> projectList) {}
}
