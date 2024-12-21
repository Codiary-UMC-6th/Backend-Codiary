package com.codiary.backend.domain.member.repository;

import com.codiary.backend.domain.member.entity.Follow;
import com.codiary.backend.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember);

    List<Follow> findByFromMemberAndFollowStatusTrueOrderByUpdatedAtDesc(Member fromMember);

    List<Follow> findByToMemberAndFollowStatusTrue(Member fromMember);
}
