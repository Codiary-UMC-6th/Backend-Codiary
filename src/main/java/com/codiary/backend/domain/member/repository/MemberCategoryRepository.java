package com.codiary.backend.domain.member.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.entity.MemberCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {
    Optional<MemberCategory> findByMemberCategoryIdAndMember(Long memberCategoryId, Member member);
}
