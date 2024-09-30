package com.codiary.backend.domain.member.repository;

import com.codiary.backend.domain.member.entity.Follow;
import com.codiary.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember);

    List<Follow> findByFromMemberAndFollowStatusTrueOrderByUpdatedAtDesc(Member fromMember);
}
