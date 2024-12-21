package com.codiary.backend.domain.alert.repository;

import com.codiary.backend.domain.alert.entity.EventCategory;
import com.codiary.backend.domain.alert.entity.EventReceive;
import com.codiary.backend.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventReceiveRepository extends JpaRepository<EventReceive, Long> {

    Optional<EventReceive> findByMemberAndEventCategory(Member member, EventCategory category);

    Boolean existsByMemberAndEventCategoryAndStatusFalse(Member member, EventCategory category);
}
