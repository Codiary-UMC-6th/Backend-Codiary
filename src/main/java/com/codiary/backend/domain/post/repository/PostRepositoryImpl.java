package com.codiary.backend.domain.post.repository;

import static com.codiary.backend.domain.member.entity.QFollow.follow;
import static com.codiary.backend.domain.member.entity.QMember.member;
import static com.codiary.backend.domain.member.entity.QMemberImage.memberImage;
import static com.codiary.backend.domain.post.entity.QPost.post;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.entity.QMember;
import com.codiary.backend.domain.post.entity.Post;
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
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        NumberExpression<Double> numberTemplate = Expressions.numberTemplate(Double.class,
                "function('match', {0}, {1}, {2})", post.postTitle, post.postBody, keyword);

        return numberTemplate.gt(0);
    }

    @Override
    public Page<Post> findPostsByMemberWithAuthorInfoOrderByDesc(Member memberEntity, Pageable pageable) {
        // 매핑: post 정보 & 작성자 정보들 & 팔로워들 & 팔로워 정보들
        // 조건: 팔로워가 요청자일 것
        QMember author = new QMember("author");
        QMember requester = new QMember("requester");

        List<Post> posts = queryFactory
                .selectDistinct(post)
                .from(post)
                .leftJoin(post.member, author)
                .leftJoin(author.image, memberImage)
                .leftJoin(follow).on(author.eq(follow.toMember))
                .leftJoin(follow.fromMember, requester)
                .where(
                        requester.eq(memberEntity)
                                .and(post.postAccess.eq(PostAccess.ENTIRE))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdAt.desc())
                .fetch();

        Long total = queryFactory
                .select(post.countDistinct())
                .from(post)
                .leftJoin(post.member, author)
                .leftJoin(follow).on(author.eq(follow.toMember))
                .leftJoin(follow.fromMember, requester)
                .where(
                        requester.eq(memberEntity)
                                .and(post.postAccess.eq(PostAccess.ENTIRE))
                )
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<Post> findPostsWithAuthorInfoOrderByCreatedAtDesc(Pageable pageable) {
        List<Post> posts = queryFactory
                .selectDistinct(post)
                .from(post)
                .leftJoin(post.member, member)
                .leftJoin(member.image, memberImage)
                .where(post.postAccess.eq(PostAccess.ENTIRE))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdAt.desc())
                .fetch();

        Long total = queryFactory
                .select(post.countDistinct())
                .from(post)
                .leftJoin(post.member, member)
                .where(post.postAccess.eq(PostAccess.ENTIRE))
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }
}
