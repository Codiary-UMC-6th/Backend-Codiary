package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.mapping.Categories;
import com.codiary.backend.global.domain.entity.mapping.MemberCategory;
import com.codiary.backend.global.web.dto.MemberCategory.MemberCategoryRequestDTO;
import com.codiary.backend.global.web.dto.MemberCategory.MemberCategoryResponseDTO;

public class MemberCategoryConverter {

    // 카테고리를 회원별 관심 카테고리에 추가
    // Controller
    public static MemberCategoryResponseDTO.CreateMemberCategoryResultDTO toCreateMemberCategoryResultDTO(MemberCategory memberCategory) {
        return MemberCategoryResponseDTO.CreateMemberCategoryResultDTO.builder()
                .memberId(memberCategory.getMember().getMemberId())
                .categoryId(memberCategory.getCategories().getCategoryId())
                .memberCategoryId(memberCategory.getMemberCategoryId())
                .categoryName(memberCategory.getCategories().getName())
                .createdAt(memberCategory.getCreatedAt())
                .build();
    }
    // Service
    public static MemberCategory toMemberCategory(Member member, Categories categories) {
        return MemberCategory.builder()
                .member(member)
                .categories(categories)
                .build();
    }

    // 회원별 관심 카테고리탭 수정
    public static MemberCategoryResponseDTO.PatchMemberCategoryResultDTO toPatchMemberCategoryResultDTO(MemberCategory memberCategory) {
        return MemberCategoryResponseDTO.PatchMemberCategoryResultDTO.builder()
                .memberCategoryId(memberCategory.getMemberCategoryId())
                .memberId(memberCategory.getMember().getMemberId())
                .categoryId(memberCategory.getCategories().getCategoryId())
                .categoryName(memberCategory.getCategories().getName())
                .updatedAt(memberCategory.getUpdatedAt())
                .build();
    }

    // 회원별 관심 카테고리탭 삭제하기
    public static MemberCategoryResponseDTO.DeleteMemberCategoryResultDTO toDeleteMemberCategoryResultDTO(Long memberCategoryId) {
        return MemberCategoryResponseDTO.DeleteMemberCategoryResultDTO.builder()
                .memberCategoryId(memberCategoryId)
                .build();
    }

}
