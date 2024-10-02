package com.codiary.backend.domain.member.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.entity.MemberProjectMap;
import com.codiary.backend.domain.member.entity.QMemberProjectMap;
import com.codiary.backend.domain.project.entity.QProject;
import com.codiary.backend.domain.team.entity.QTeam;
import com.codiary.backend.domain.team.entity.QTeamMember;
import com.codiary.backend.domain.team.entity.TeamMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.codiary.backend.domain.member.entity.QMember.member;
import static com.codiary.backend.domain.techstack.entity.QTechStacks.techStacks;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public Optional<Member> findMemberWithTechStacksAndProjectsAndTeam(Long userId) {
        Member fetchedMember = queryFactory
                .selectFrom(member)
                .leftJoin(member.techStackList, techStacks).fetchJoin()
                .where(member.memberId.eq(userId))
                .fetchOne();

        fetchMemberProjects(userId, fetchedMember);

        fetchTeamMembers(userId, fetchedMember);

        return Optional.ofNullable(fetchedMember);
    }

    private void fetchMemberProjects(Long userId, Member fetchedMember) {
        List<MemberProjectMap> projects = queryFactory
                .selectFrom(QMemberProjectMap.memberProjectMap)
                .leftJoin(QMemberProjectMap.memberProjectMap.project, QProject.project)
                .where(QMemberProjectMap.memberProjectMap.member.memberId.eq(userId))
                .fetch();

        fetchedMember.setMemberProjectMapList(projects);
    }

    private void fetchTeamMembers(Long userId, Member fetchedMember) {
        List<TeamMember> teamMembers = queryFactory
                .selectFrom(QTeamMember.teamMember)
                .leftJoin(QTeamMember.teamMember.team, QTeam.team)
                .where(QTeamMember.teamMember.member.memberId.eq(userId))
                .fetch();

        fetchedMember.setTeamMemberList(teamMembers);
    }
}
