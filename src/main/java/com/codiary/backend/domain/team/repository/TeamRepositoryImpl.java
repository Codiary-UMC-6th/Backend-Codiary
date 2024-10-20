package com.codiary.backend.domain.team.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.team.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.codiary.backend.domain.member.entity.QMember.member;
import static com.codiary.backend.domain.team.entity.QTeam.team;
import static com.codiary.backend.domain.team.entity.QTeamFollow.teamFollow;
import static com.codiary.backend.domain.team.entity.QTeamMember.teamMember;

@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Team> findTeamProfile(Long teamId){
        Team fetchedTeam = queryFactory
                .selectFrom(team)
                .leftJoin(team.teamMemberList, teamMember).fetchJoin()
                .leftJoin(teamMember.member, member).fetchJoin()
                .where(team.deletedAt.isNull()
                        .and(team.teamId.eq(teamId)))
                .fetchOne();

        return Optional.ofNullable(fetchedTeam);
    }

    @Override
    public boolean isTeamMember(Team fetchedTeam, Member member) {
        return queryFactory
                .selectOne()
                .from(teamMember)
                .where(teamMember.team.teamId.eq(fetchedTeam.getTeamId())
                        .and(teamMember.member.memberId.eq(member.getMemberId())))
                .fetchFirst() != null;
    }

    @Override
    public Optional<Team> findByIdWithFollowers(Long teamId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(team)
                .leftJoin(team.followers, teamFollow).fetchJoin()
                .leftJoin(teamFollow.member).fetchJoin()
                .where(team.teamId.eq(teamId))
                .fetchOne());
    }
}
