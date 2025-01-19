package com.codiary.backend.domain.techstack.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.techstack.entity.TechStacks;
import com.codiary.backend.domain.techstack.enumerate.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechStackRepository extends JpaRepository<TechStacks, Long> {
    boolean existsByNameAndMember(TechStack name, Member member);
}
