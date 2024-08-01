package com.codiary.backend.global.service.MemberCategoryService;

import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.converter.MemberCategoryConverter;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.mapping.Categories;
import com.codiary.backend.global.domain.entity.mapping.MemberCategory;
import com.codiary.backend.global.repository.CategoryRepository;
import com.codiary.backend.global.repository.MemberCategoryRepository;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.web.dto.MemberCategory.MemberCategoryRequestDTO;
import com.codiary.backend.global.web.dto.MemberCategory.MemberCategoryResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberCategoryCommandServiceImpl implements MemberCategoryCommandService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final MemberCategoryRepository memberCategoryRepository;

    // 카테고리를 회원별 관심 카테고리에 추가
    @Override
    public MemberCategory createMemberCategory(Long memberId, Long categoryId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Categories categories = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND));

        MemberCategory memberCategory = MemberCategoryConverter.toMemberCategory(member, categories);

        return memberCategoryRepository.save(memberCategory);

    }

    // 회원별 관심 카테고리탭 수정하기
    @Override
    public MemberCategory patchMemberCategory(Long memberCategoryId, MemberCategoryRequestDTO.PatchMemberCategoryResultDTO request) {

        MemberCategory memberCategory = memberCategoryRepository.findById(memberCategoryId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBERCATEGORY_NOT_FOUND));

        if (request != null) {
            Categories categories = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND));

            memberCategory.patchCategories(categories);
        }

        return memberCategoryRepository.save(memberCategory);

    }

    // 회원별 관심 카테고리탭 삭제하기
    @Override
    public void deleteMemberCategory(Long memberCategoryId) {

        MemberCategory memberCategory = memberCategoryRepository.findById(memberCategoryId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND));

        memberCategoryRepository.delete(memberCategory);

    }

}
