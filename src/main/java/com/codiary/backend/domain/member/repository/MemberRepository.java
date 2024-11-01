package com.codiary.backend.domain.member.repository;

import com.codiary.backend.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNicknameIgnoreCase(String nickname);
}