package com.codiary.backend.domain.post.repository;

import static com.codiary.backend.domain.member.entity.QFollow.follow;
import static com.codiary.backend.domain.member.entity.QMember.member;
import static com.codiary.backend.domain.member.entity.QMemberImage.memberImage;
import static com.codiary.backend.domain.post.entity.QPost.post;

import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codiary.backend.domain.post.entity.QPost.post;
import static com.codiary.backend.domain.project.entity.QProject.project;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> searchPost(Long memberId, String keyword, Pageable pageable) {
        List<Post> postList = queryFactory
                .selectDistinct(post)
                .from(post)
                .leftJoin(post.member, member)
                .leftJoin(member.image, memberImage)
                .where(keywordEq(keyword)) // Full-Text Search 조건
                .where(canAccess(memberId))
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
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        NumberExpression<Double> numberTemplate = Expressions.numberTemplate(Double.class,
                "function('match', {0}, {1}, {2})", post.postTitle, post.postBody, keyword);

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
    
    @Override
    public Page<Post> getLatestPostsOfFollowings(Long memberId, Pageable pageable) {
        List<Post> posts = queryFactory
                .selectDistinct(post)
                .from(post)
                .leftJoin(post.member, member)
                .leftJoin(member.image, memberImage)
                .leftJoin(member.followers, follow)
                .where(follow.followStatus.eq(true)
                        .and(follow.fromMember.memberId.eq(memberId).and(canAccess(memberId))))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.countDistinct())
                .from(post)
                .leftJoin(post.member, member)
                .leftJoin(member.followers, follow)
                .where(follow.followStatus.eq(true)
                        .and(follow.fromMember.memberId.eq(memberId).and(canAccess(memberId))))
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<Post> getLatestPosts(Pageable pageable) {
        List<Post> posts = queryFactory
                .selectDistinct(post)
                .from(post)
                .leftJoin(post.member, member)
                .leftJoin(member.image, memberImage)
                .where(post.postAccess.eq(PostAccess.ENTIRE))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.countDistinct())
                .from(post)
                .leftJoin(post.member, member)
                .where(post.postAccess.eq(PostAccess.ENTIRE))
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<Post> getPopularPosts(Pageable pageable) {
        List<Post> posts = queryFactory
                .selectDistinct(post)
                .from(post)
                .where(post.postAccess.eq(PostAccess.ENTIRE))
                .orderBy(post.commentList.size().add(post.bookmarkList.size()).desc(),
                        post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.countDistinct())
                .from(post)
                .where(post.postAccess.eq(PostAccess.ENTIRE))
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<Post> getPopularPostsByCategoryId(Long memberId, Long categoryId, Pageable pageable) {
        List<Post> posts = queryFactory
                .selectDistinct(post)
                .from(post)
                .where(post.categoriesList.any().categoryId.eq(categoryId).and(canAccess(memberId)))
                .orderBy(post.commentList.size().add(post.bookmarkList.size()).desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.countDistinct())
                .from(post)
                .where(post.categoriesList.any().categoryId.eq(categoryId).and(canAccess(memberId)))
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }

    private BooleanExpression canAccess(Long memberId) {
        return post.postAccess.eq(PostAccess.ENTIRE)
                .or(post.postAccess.eq(PostAccess.TEAM)
                        .and(post.team.teamMemberList.any().member.memberId.eq(memberId)));
    }
}
