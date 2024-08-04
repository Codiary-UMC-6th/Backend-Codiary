package com.codiary.backend.global.web.dto.MemberCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberCategoryRequestDTO {

    // 회원별 관심 카테고리탭 수정하기
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatchMemberCategoryResultDTO {
        Long categoryId;
    }

}
