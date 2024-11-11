package com.codiary.backend.domain.category.service;

import com.codiary.backend.domain.category.entity.Category;
import com.codiary.backend.domain.category.repository.CategoryRepository;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.entity.MemberCategory;
import com.codiary.backend.domain.member.repository.MemberCategoryRepository;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final MemberCategoryRepository memberCategoryRepository;

    @Transactional
    public MemberCategory createCategory(String categoryName, Long memberId) {
        //validation: 멤버인지, 카테고리 이름이 이미 존재하는지 확인, 멤버가 이미 카테고리 설정했는지 확인
        Category category = categoryRepository.findByName(categoryName)
                .orElseGet(() ->categoryRepository.save(
                        Category.builder()
                                .name(categoryName)
                                .build()
                ));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if(member.getMemberCategoryList().stream().anyMatch(memberCategory -> memberCategory.getCategory().getName().equals(categoryName))) {
            throw new GeneralException(ErrorStatus.CATEGORY_ALREADY_EXISTS);
        }

        //business logic: 카테고리 생성
        MemberCategory memberCategory = MemberCategory.builder()
                .member(member)
                .category(category)
                .build();

        //response: 카테고리 반환
        return memberCategoryRepository.save(memberCategory);
    }

    public List<MemberCategory> getCategoryList(Long memberId){
        //validation: 멤버인지 확인
        Member member = memberRepository.findByIdWithCategory(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //business logic & return: 카테고리 조회
        return member.getMemberCategoryList();
    }

    @Transactional
    public void deleteCategory(Long memberCategoryId, Long memberId) {
        // validation: 멤버인지, 카테고리가 존재하는지 확인
        Member member = memberRepository.findByIdWithCategory(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        MemberCategory memberCategory = memberCategoryRepository.findByMemberCategoryIdAndMember(memberCategoryId, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND));

        // business logic: 카테고리 삭제
        memberCategory.getMember().getMemberCategoryList().remove(memberCategory);
        memberCategory.getCategory().getMemberCategoryList().remove(memberCategory);
        memberCategoryRepository.delete(memberCategory);
    }


    @Transactional
    public Category addCategory(Post post, String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseGet(() -> categoryRepository.save(Category.builder()
                        .name(categoryName)
                        .build()));
    }
}
