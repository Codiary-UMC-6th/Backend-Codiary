package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.mapping.MemberCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {

    // 회원별 관심 카테고리탭 리스트 조회
    @Query("SELECT mc FROM MemberCategory mc JOIN FETCH mc.member m JOIN FETCH mc.categories c WHERE m = :member")
    List<MemberCategory> findAllByMember(Member member);

}
