package com.codiary.backend.global.service.MemberCategoryService;

import com.codiary.backend.global.domain.entity.mapping.MemberCategory;
import com.codiary.backend.global.web.dto.MemberCategory.MemberCategoryRequestDTO;
import com.codiary.backend.global.web.dto.MemberCategory.MemberCategoryResponseDTO;

public interface MemberCategoryCommandService {

    // 카테고리를 회원별 관심 카테고리에 추가
    MemberCategory createMemberCategory(Long memberId, Long categoryId);

    // 회원별 관심 카테고리탭 수정하기
    MemberCategory patchMemberCategory(Long memberCategoryId, MemberCategoryRequestDTO.PatchMemberCategoryResultDTO request);

    // 회원별 관심 카테고리탭 삭제하기
    void deleteMemberCategory(Long memberCategoryId);

}
