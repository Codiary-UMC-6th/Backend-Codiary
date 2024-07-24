package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
