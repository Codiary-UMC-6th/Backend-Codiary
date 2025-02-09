package com.codiary.backend.domain.post.repository;

import static com.codiary.backend.domain.member.entity.QFollow.follow;
import static com.codiary.backend.domain.member.entity.QMember.member;
import static com.codiary.backend.domain.member.entity.QMemberImage.memberImage;
import static com.codiary.backend.domain.post.entity.QBookmark.bookmark;
import static com.codiary.backend.domain.post.entity.QPost.post;
import static com.codiary.backend.domain.project.entity.QProject.project;
import static com.codiary.backend.domain.team.entity.QTeam.team;
import static com.codiary.backend.domain.team.entity.QTeamFollow.teamFollow;
import static com.codiary.backend.domain.team.entity.QTeamProfileImage.teamProfileImage;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.team.entity.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
                .leftJoin(post.team, team).fetchJoin()
                .orderBy(getOrderBy(pageable.getSort()))
                .where(keywordEq(keyword)) // Full-Text Search 조건
                .where(canAccess(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(keywordEq(keyword))  // Full-Text Search 조건
                .where(canAccess(memberId))
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
    public Page<Post> getPostList(Pageable pageable) {
        OrderSpecifier[] orderSpecifiers = getOrderBy(pageable.getSort());

        List<Post> posts = queryFactory
                .selectDistinct(post)
                .from(post)
                .leftJoin(post.team, team).fetchJoin()
                .where(post.postAccess.eq(PostAccess.ENTIRE))
                .orderBy(orderSpecifiers)
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

    private OrderSpecifier[] getOrderBy(Sort sort) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : sort) {
            if (order.getProperty().equals("latest")) {
                orderSpecifiers.add(
                        new OrderSpecifier<>(
                                Order.DESC,
                                post.createdAt
                        )
                );
            } else if (order.getProperty().equals("popular")) {
                orderSpecifiers.add(
                        new OrderSpecifier<>(
                                Order.DESC,
                                post.commentList.size().add(post.bookmarkList.size())
                        )
                );
                orderSpecifiers.add(
                        new OrderSpecifier<>(
                                Order.DESC,
                                post.createdAt
                        )
                );
            }
        }

        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }

    @Override
    public Page<Post> getPostsByCategoryId(Long memberId, Long categoryId, Pageable pageable) {
        List<Post> posts = queryFactory
                .selectDistinct(post)
                .from(post)
                .leftJoin(post.team, team).fetchJoin()
                .where(post.categoriesList.any().categoryId.eq(categoryId).and(canAccess(memberId)))
                .orderBy(getOrderBy(pageable.getSort()))
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

    public Page<Post> findByBookmarkPostList(Member member, Pageable pageable) {
        List<Post> postList = queryFactory
                .select(post)
                .distinct()
                .from(post)
                .leftJoin(post.bookmarkList, bookmark).fetchJoin()
                .leftJoin(post.team, team).fetchJoin()
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

    @Override
    public Page<Post> getPostsByName(
            Long memberId, String authorName, String teamName, String projectName, Pageable pageable
    ) {
        BooleanBuilder booleanBuilder = searchBy(authorName, teamName, projectName);

        List<Post> posts = queryFactory
                .selectDistinct(post)
                .from(post)
                // member join
                .leftJoin(post.member, member)
                .leftJoin(member.image, memberImage)
                // team join
                .leftJoin(post.team, team)
                .leftJoin(team.profileImage, teamProfileImage)
                // project join
                .leftJoin(post.project, project)
                // 조건 탐색
                .where(
                        canAccess(memberId).and(booleanBuilder)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.countDistinct())
                .from(post)
                // 조건 탐색
                .where(
                        canAccess(memberId).and(booleanBuilder)
                )
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }

    private BooleanBuilder searchBy(String authorName, String teamName, String projectName) {
        if (!authorName.isEmpty()) {
            return new BooleanBuilder().and(member.nickname.contains(authorName));
        } else if (!teamName.isEmpty()) {
            return new BooleanBuilder().and(team.name.contains(teamName));
        } else if (!projectName.isEmpty()) {
            return new BooleanBuilder().and(project.projectName.contains(projectName));
        }
        return new BooleanBuilder();
    }

    @Override
    public Optional<Post> findByIdWithTeam(Long postId, Long requesterId) {
        Optional<Post> fetchedPost = Optional.ofNullable(queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.team, team).fetchJoin()
                .leftJoin(team.profileImage, teamProfileImage).fetchJoin()
                .where(post.postId.eq(postId).and(canAccess(requesterId)))
                .fetchFirst()
        );
        return fetchedPost;
    }

    @Override
    public Page<Post> findAllByPostTitleContainingIgnoreCaseOrderByCreatedAtDesc(String postTitle, Pageable pageable) {
        List<Post> posts = queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.team, team).fetchJoin()
                .leftJoin(team.profileImage, teamProfileImage).fetchJoin()
                .where(post.postTitle.containsIgnoreCase(postTitle))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.countDistinct())
                .from(post)
                .where(post.postTitle.containsIgnoreCase(postTitle))
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable) {
        List<Post> posts = queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.team, team).fetchJoin()
                .leftJoin(team.profileImage, teamProfileImage).fetchJoin()
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.countDistinct())
                .from(post)
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Optional<Post> findTopByTeamAndPostIdLessThanOrderByCreatedAtDescPostIdDesc(Team findTeam, Long postId) {
        Optional<Post> fetchedPost = Optional.ofNullable(queryFactory
                .select(post)
                .from(post)
                .join(post.team, team).fetchJoin()
                .leftJoin(team.profileImage, teamProfileImage).fetchJoin()
                .where(post.team.eq(findTeam).and(post.postId.lt(postId)))
                .orderBy(post.createdAt.desc(), post.postId.desc())
                .fetchFirst());

        return fetchedPost;
    }

    @Override
    public Optional<Post> findTopByTeamAndPostIdGreaterThanOrderByCreatedAtAscPostIdAsc(Team findTeam, Long postId) {
        Optional<Post> fetchedPost = Optional.ofNullable(queryFactory
                .select(post)
                .from(post)
                .join(post.team, team).fetchJoin()
                .leftJoin(team.profileImage, teamProfileImage).fetchJoin()
                .where(post.team.eq(findTeam).and(post.postId.gt(postId)))
                .orderBy(post.createdAt.desc(), post.postId.desc())
                .fetchFirst());

        return fetchedPost;
    }

    @Override
    public Optional<Post> findTopByMemberAndPostIdLessThanOrderByCreatedAtDescPostIdDesc(Member member, Long postId) {
        Optional<Post> fetchedPost = Optional.ofNullable(queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.team, team).fetchJoin()
                .leftJoin(team.profileImage, teamProfileImage).fetchJoin()
                .where(post.member.eq(member).and(post.postId.lt(postId)))
                .orderBy(post.createdAt.desc(), post.postId.desc())
                .fetchFirst());

        return fetchedPost;
    }

    @Override
    public Optional<Post> findTopByMemberAndPostIdGreaterThanOrderByCreatedAtAscPostIdAsc(Member member, Long postId) {
        Optional<Post> fetchedPost = Optional.ofNullable(queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.team, team).fetchJoin()
                .leftJoin(team.profileImage, teamProfileImage).fetchJoin()
                .where(post.member.eq(member).and(post.postId.gt(postId)))
                .orderBy(post.createdAt.desc(), post.postId.desc())
                .fetchFirst());

        return fetchedPost;
    }
}
