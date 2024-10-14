package com.codiary.backend.domain.member.repository;

import com.codiary.backend.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<Member> findMemberWithTechStacksAndProjectsAndTeam(Long userId);
    Optional<Member> findMemberWithTechStacks(Long userId);

    Optional<Member> findByIdWithFollowedTeams(Long id);
    Optional<Member> findByIdWithAndFollowersAndFollowings(Long id);

    Optional<Member> findByIdWithFollowings(Long id);
    Optional<Member> findByIdWithFollowers(Long id);
}
