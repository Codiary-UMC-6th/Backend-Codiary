package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Follow;
import com.codiary.backend.global.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember);

    List<Follow> findByFromMemberAndFollowStatus(Member member, boolean status);

    List<Follow> findByToMemberAndFollowStatus(Member member, boolean status);
}
