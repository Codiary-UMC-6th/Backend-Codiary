package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이미 가입된 메일인지 확인
    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m LEFT JOIN m.followers LEFT JOIN m.followings WHERE m = :member")
    Optional<Member> findByMemberWithAndFollowersAndFollowings(@Param("member")Member member);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.followings WHERE m.memberId = :toId")
    Optional<Member> findByToIdWithFollowings(@Param("toId") long id);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.followers WHERE m.memberId = :toId")
    Optional<Member> findByToIdWithFollowers(@Param("toId") long id);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.techStackList WHERE m.memberId = :memberId")
    Member findMemberWithTechStacks(@Param("memberId") Long memberId);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.teamMemberList tm LEFT JOIN FETCH tm.team WHERE m.memberId = :memberId")
    Member findMemberWithTeam(@Param("memberId") Long memberId);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.memberProjectMapList mp LEFT JOIN FETCH mp.project WHERE m.memberId = :memberId")
    Member findMemberWithProjects(@Param("memberId") Long memberId);
}