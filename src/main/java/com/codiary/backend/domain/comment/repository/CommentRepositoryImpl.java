package com.codiary.backend.domain.comment.repository;

import static com.codiary.backend.domain.comment.entity.QComment.comment;
import static com.codiary.backend.domain.member.entity.QMember.member;
import static com.codiary.backend.domain.post.entity.QPost.post;

import com.codiary.backend.domain.comment.entity.Comment;
import com.codiary.backend.domain.comment.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> findByPostWithMemberInfoAndRepliesOrderByCreatedAtAsc(Long postId, Pageable pageable) {
        QComment child = new QComment("child");
        List<Comment> comments = queryFactory
                .selectFrom(comment)
                .leftJoin(comment.member, member)
                .leftJoin(comment.post, post)
                .leftJoin(comment.childComments, child).fetchJoin()
                .where(comment.post.postId.eq(postId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.createdAt.asc())
                .fetch();

        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.post.postId.eq(postId))
                .fetchOne();

        return new PageImpl<>(comments, pageable, total);
    }

    @Override
    public Page<Comment> findByParentWithMemberInfoOrderByCreatedAtAsc(Long commentId, Pageable pageable) {
        QComment parent = new QComment("parent");
        List<Comment> comments = queryFactory
                .selectFrom(comment)
                .leftJoin(comment.member, member)
                .leftJoin(comment.parent, parent)
                .where(comment.parent.commentId.eq(commentId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.createdAt.asc())
                .fetch();

        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.parent.commentId.eq(commentId))
                .fetchOne();

        return new PageImpl<>(comments, pageable, total);
    }

    @Override
    public Optional<Comment> findByIdWithReplies(Long commentId) {
        QComment child = new QComment("child");
        Comment fetchedComment = queryFactory
                .selectFrom(comment)
                .leftJoin(comment.member, member)
                .leftJoin(comment.post, post)
                .leftJoin(comment.childComments, child).fetchJoin()
                .where(comment.commentId.eq(commentId))
                .orderBy(comment.createdAt.asc())
                .fetchOne();

        return Optional.ofNullable(fetchedComment);
    }
}
