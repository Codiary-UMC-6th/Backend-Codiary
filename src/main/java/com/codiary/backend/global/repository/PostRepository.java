package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.*;
import com.codiary.backend.global.domain.entity.mapping.MemberCategory;
import com.codiary.backend.global.web.dto.Post.PostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByPostTitleContainingIgnoreCaseOrderByCreatedAtDesc(String postTitle, Pageable pageable);
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Post> findByMemberOrderByCreatedAtDescPostIdDesc(Member member, Pageable pageable);
    Page<Post> findByTeamOrderByCreatedAtDescPostIdDesc(Team team, Pageable pageable);
    Page<Post> findByProjectAndMemberOrderByCreatedAtDescPostIdDesc(Project project, Member member, Pageable pageable);
    Page<Post> findByProjectAndTeamOrderByCreatedAtDescPostIdDesc(Project project, Team team, Pageable pageable);
    Page<Post> findByTeamAndMemberOrderByCreatedAtDescPostIdDesc(Team team, Member member, Pageable pageable);
    Page<Post> findByAuthorsList_MemberOrderByCreatedAtDescPostIdDesc(Member member, Pageable pageable);
    Page<Post> findByProjectAndAuthorsList_MemberOrderByCreatedAtDescPostIdDesc(Project project, Member member, Pageable pageable);
    Page<Post> findByTeamAndAuthorsList_MemberOrderByCreatedAtDescPostIdDesc(Team team, Member member, Pageable pageable);
    boolean existsByTeam(Team team);
    boolean existsByProject(Project project);
    boolean existsByMember(Member member);

    Optional<Post> findTopByPostIdLessThanOrderByCreatedAtDescPostIdDesc(Long postId);

    Optional<Post> findTopByPostIdGreaterThanOrderByCreatedAtAscPostIdAsc(Long postId);


    @Query("SELECT p FROM Post p JOIN FETCH p.project WHERE p.member = :member AND p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.createdAt ASC")
    List<Post> findByMemberAndCreatedAtBetweenOrderByCreatedAtAsc(@Param("member") Member member, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT p.postId FROM Post p JOIN p.categoriesList c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))")
    List<Long> findPostIdsByCategoryName(@Param("categoryName") String categoryName);

    Page<Post> findByPostIdIn(List<Long> postIds, Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN p.authorsList a LEFT JOIN p.project j WHERE (p.member = :member OR a.member = :member) AND j.projectId = :projectId ORDER BY p.createdAt DESC")
    Page<Post> findPostsByMemberOrAuthorAndProjectId(@Param("member") Member member, @Param("projectId") Long projectId, Pageable pageable);

    // 메인페이지 인기글 전체 리스트 조회
    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.bookmarkList b " +
            "LEFT JOIN p.commentList c " +
            "GROUP BY p " +
            "ORDER BY (COUNT(b) + COUNT(c)) DESC")
    Page<Post> findAllByBookmarkAndCommentCount(Pageable pageable);

    // 메인페이지 인기글 멤버 관심 카테고리별 리스트 조회
    @Query("SELECT p FROM Post p " +
            "JOIN p.categoriesList c " +
            "JOIN c.memberCategoryList mc " +
            "WHERE mc = :memberCategory " +
            "GROUP BY p " +
            "ORDER BY (SIZE(p.bookmarkList) + SIZE(p.commentList)) DESC")
    Page<Post> findPostsByMemberCategorySorted(MemberCategory memberCategory, Pageable pageable);

    // 메인페이지 최신글 리스트 조회
//    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 메인페이지 팔로잉 게시글 리스트 조회
    Page<Post> findAllByMemberOrderByCreatedAtDesc(Member toMember, Pageable pageable);

    // 제목 & 본문 & 저자 & 프로젝트 & 카테고리 검색
//    Page<Post> findAllByPostTitleContainingOrPostBodyContainingOrMemberContainingOrProjectContainingOrCategoriesListContaining(String keywordTitle, String keywordBody, String keywordMember, String keywordProject, String keywordCategories, Pageable pageable);
    @Query("SELECT DISTINCT p FROM Post p " +
            "JOIN p.member m " +
            "LEFT JOIN p.project proj " +
            "LEFT JOIN p.categoriesList c " +
            "WHERE p.postTitle LIKE %:keyword% " +
            "OR p.postBody LIKE %:keyword% " +
            "OR m.nickname LIKE %:keyword% " +
            "OR proj.projectName LIKE %:keyword% " +
            "OR c.name LIKE %:keyword%")
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);

}