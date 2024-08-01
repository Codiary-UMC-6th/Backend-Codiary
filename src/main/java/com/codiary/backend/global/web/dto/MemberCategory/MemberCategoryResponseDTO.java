package com.codiary.backend.global.web.dto.MemberCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemberCategoryResponseDTO {

    // 카테고리를 회원별 관심 카테고리에 추가
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMemberCategoryResultDTO {
        Long memberId;
        Long categoryId;
        Long memberCategoryId;
        String categoryName;
        LocalDateTime createdAt;
    }

    // 회원별 관심 카테고리탭 수정하기
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatchMemberCategoryResultDTO {
        Long memberCategoryId;
        Long memberId;
        Long categoryId;
        String categoryName;
        LocalDateTime updatedAt;
    }

    // 회원별 관심 카테고리탭 삭제하기
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteMemberCategoryResultDTO {
        Long memberCategoryId;
    }

}
