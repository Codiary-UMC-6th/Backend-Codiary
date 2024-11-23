package com.codiary.backend.domain.post.repository;

import com.codiary.backend.domain.member.entity.Member;
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

import static com.codiary.backend.domain.post.entity.QBookmark.bookmark;
import static com.codiary.backend.domain.post.entity.QPost.post;
import static com.codiary.backend.domain.project.entity.QProject.project;

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

    public Page<Post> findByBookmarkPostList(Member member, Pageable pageable) {
        List<Post> postList = queryFactory
                .select(post)
                .distinct()
                .from(post)
                .leftJoin(post.bookmarkList, bookmark).fetchJoin()
                .where(bookmark.member.memberId.eq(member.getMemberId())
                        .and(post.deletedAt.isNull()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(bookmark.createdAt.desc(), bookmark.id.desc())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .distinct()
                .from(post)
                .leftJoin(post.bookmarkList, bookmark)
                .where(bookmark.member.memberId.eq(member.getMemberId())
                        .and(post.deletedAt.isNull()))
                .fetchOne();

        return new PageImpl<>(postList, pageable, total);
    }
}
