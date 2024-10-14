package com.codiary.backend.domain.team.repository;

import com.codiary.backend.domain.team.entity.TeamFollow;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.codiary.backend.domain.team.entity.QTeamFollow.teamFollow;

@RequiredArgsConstructor
public class TeamFollowRepositoryImpl implements TeamFollowRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TeamFollow> findFollowersByTeamId(Long teamId) {
        List<TeamFollow> teamFollowers = queryFactory
                .selectFrom(teamFollow)
                .where(teamFollow.team.teamId.eq(teamId))
                .where(teamFollow.followStatus.eq(Boolean.TRUE))
                .fetch();

        return teamFollowers;
    }
}
