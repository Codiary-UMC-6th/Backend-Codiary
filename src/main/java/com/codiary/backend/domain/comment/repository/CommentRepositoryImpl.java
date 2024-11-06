package com.codiary.backend.domain.comment.repository;

import static com.codiary.backend.domain.comment.entity.QComment.comment;
import static com.codiary.backend.domain.member.entity.QMember.member;
import static com.codiary.backend.domain.post.entity.QPost.post;

import com.codiary.backend.domain.comment.entity.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findByPostWithMemberInfoOrderByCreatedAtDesc(Long postId) {
        List<Comment> comments = queryFactory
                .selectFrom(comment)
                .leftJoin(comment.member, member)
                .leftJoin(comment.post, post)
                .where(comment.post.postId.eq(postId))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
                .fetch();

        return comments;
    }
}
