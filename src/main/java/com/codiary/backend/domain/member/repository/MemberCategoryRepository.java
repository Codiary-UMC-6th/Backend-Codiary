package com.codiary.backend.domain.member.repository;

import com.codiary.backend.domain.member.entity.MemberCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {
}
