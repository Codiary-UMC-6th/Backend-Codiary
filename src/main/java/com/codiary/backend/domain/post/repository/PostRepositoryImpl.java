package com.codiary.backend.domain.post.repository;

import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.project.entity.Project;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codiary.backend.domain.member.entity.QFollow.follow;
import static com.codiary.backend.domain.member.entity.QMember.member;
import static com.codiary.backend.domain.post.entity.QPost.post;
import static com.codiary.backend.domain.project.entity.QProject.project;
import static com.codiary.backend.domain.team.entity.QTeam.team;
import static com.codiary.backend.domain.team.entity.QTeamFollow.teamFollow;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public Page<Post> searchPost(String keyword, Pageable pageable) {
        List<Post> postList = queryFactory
                .selectDistinct(post)
                .from(post)
                .where(keywordEq(keyword)) // Full-Text Search 조건
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(keywordEq(keyword))  // Full-Text Search 조건
                .fetchOne();

        return new PageImpl<>(postList, pageable, total);
    }

    private BooleanExpression keywordEq(String keyword) {
        if(keyword == null || keyword.isEmpty()) {
            return null;
        }
        NumberExpression<Double> numberTemplate = Expressions.numberTemplate(Double.class,"function('match', {0}, {1}, {2})", post.postTitle, post.postBody, keyword);

        return numberTemplate.gt(0);
    }

    public Map<Project, List<Post>> findPostsForCalendar(Long memberId, LocalDate date) {
        List<Post> posts = queryFactory
                .selectFrom(post)
                .leftJoin(post.project, project).fetchJoin()
                .where(post.member.memberId.eq(memberId)
                        .and(post.createdAt.between(date.atStartOfDay(), date.atTime(23, 59, 59))))
                .fetch();

        return posts.stream()
                .collect(Collectors.groupingBy(
                        Post::getProject,
                        Collectors.collectingAndThen(Collectors.toList(), ArrayList::new)
                ));
    }

    public Page<Post> findPostsByFollowing(Long id, Pageable pageable) {
        // 게시글 데이터 조회
        List<Post> posts = queryFactory
                .selectFrom(post)
                .leftJoin(post.member, member)
                .leftJoin(post.team, team)
                .leftJoin(follow).on(
                        follow.toMember.eq(member)
                                .and(follow.fromMember.memberId.eq(id))
                                .and(follow.followStatus.eq(true)))
                .leftJoin(teamFollow).on(
                        teamFollow.team.eq(team)
                                .and(teamFollow.member.memberId.eq(id))
                                .and(teamFollow.followStatus.eq(true)))
                .where(
                        // 내가 팔로우한 유저의 게시글 또는 팔로우한 팀의 게시글
                        (follow.toMember.isNotNull())
                                .or(teamFollow.isNotNull().and(team.deletedAt.isNull()))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 계산
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .leftJoin(post.member, member)
                .leftJoin(post.team, team)
                .leftJoin(follow).on(
                        follow.toMember.eq(member)
                                .and(follow.fromMember.memberId.eq(id))
                                .and(follow.followStatus.eq(true)))
                .leftJoin(teamFollow).on(
                        teamFollow.team.eq(team)
                                .and(teamFollow.member.memberId.eq(id))
                                .and(teamFollow.followStatus.eq(true)))
                .where(
                        (follow.toMember.isNotNull())
                                .or(teamFollow.isNotNull().and(team.deletedAt.isNull()))
                )
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }
}
