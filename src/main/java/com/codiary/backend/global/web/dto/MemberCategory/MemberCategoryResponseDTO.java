package com.codiary.backend.global.web.dto.MemberCategory;

import lombok.Builder;

import java.time.LocalDateTime;

public class MemberCategoryResponseDTO {

    // 카테고리를 회원별 관심 카테고리에 추가
    @Builder
    public record CreateMemberCategoryResultDTO(
            Long memberId,
            Long categoryId,
            Long memberCategoryId,
            String categoryName,
            LocalDateTime createdAt) {}

    // 회원별 관심 카테고리탭 수정하기
    @Builder
    public record PatchMemberCategoryResultDTO(
            Long memberCategoryId,
            Long memberId,
            Long categoryId,
            String categoryName,
            LocalDateTime updatedAt) {}

    // 회원별 관심 카테고리탭 삭제하기
    @Builder
    public record DeleteMemberCategoryResultDTO(Long memberCategoryId) {}
}
