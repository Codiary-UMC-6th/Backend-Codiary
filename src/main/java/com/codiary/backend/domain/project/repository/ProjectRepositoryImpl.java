package com.codiary.backend.domain.project.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.project.entity.Project;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codiary.backend.domain.post.entity.QPost.post;
import static com.codiary.backend.domain.project.entity.QProject.project;
import static com.codiary.backend.domain.team.entity.QTeam.team;
import static com.codiary.backend.domain.team.entity.QTeamMember.teamMember;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Map<LocalDate, List<Project>> findProjectsForCalendar(Long memberId, LocalDate startDate, LocalDate endDate) {
        // 한 달 내의 게시물 목록을 조회
        List<Post> posts = queryFactory
                .selectFrom(post)
                .leftJoin(post.project, project).fetchJoin()
                .where(post.member.memberId.eq(memberId)
                        .and(post.createdAt.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59))))
                .fetch();

        // 게시물 목록을 날짜별로 그룹화하고 프로젝트로 매핑
        return posts.stream()
                .collect(Collectors.groupingBy(
                        post -> post.getCreatedAt().toLocalDate(),
                        Collectors.mapping(Post::getProject, Collectors.collectingAndThen(Collectors.toSet(), ArrayList::new))
                ));
    }

    public List<Project> findByMemberProjectMapsMember(Member member) {
        return queryFactory
                .selectFrom(project)
                .leftJoin(project.member)
                .leftJoin(project.team, team)
                .leftJoin(team.teamMemberList, teamMember)
                .where(
                        project.member.eq(member)
                                .and(project.deletedAt.isNull())
                                .or(
                                        team.teamMemberList.any().member.eq(member)
                                                .and(team.deletedAt.isNull())
                                )
                )
                .fetch();
    }
}
