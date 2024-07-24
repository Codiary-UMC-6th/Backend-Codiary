package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이미 가입된 메일인지 확인
    Boolean existsByEmail(String email);
}
