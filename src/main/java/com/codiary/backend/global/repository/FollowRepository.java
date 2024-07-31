package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Follow;
import com.codiary.backend.global.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query("select f from Follow f join fetch f.fromMember join fetch f.toMember where f.fromMember = :fromMember and f.toMember = :toMember")
    Optional<Follow> findByFromMemberAndToMember(@Param("fromMember") Member fromMember, @Param("toMember") Member toMember);

    List<Follow> findByFromMemberAndFollowStatusTrueOrderByUpdatedAt(Member member);

    List<Follow> findByToMemberAndFollowStatusTrueOrderByUpdatedAt(Member member);

}
