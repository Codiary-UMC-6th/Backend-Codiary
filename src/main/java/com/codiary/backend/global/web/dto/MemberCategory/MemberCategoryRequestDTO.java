package com.codiary.backend.global.web.dto.MemberCategory;

import lombok.Builder;

public class MemberCategoryRequestDTO {

    // 회원별 관심 카테고리탭 수정하기 요청 DTO
    @Builder
    public record PatchMemberCategoryResultDTO (Long categoryId) {}

}
