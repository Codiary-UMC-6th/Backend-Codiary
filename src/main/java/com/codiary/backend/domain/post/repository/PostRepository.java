package com.codiary.backend.domain.post.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.team.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Page<Post> findAllByPostTitleContainingIgnoreCaseOrderByCreatedAtDesc(String postTitle, Pageable pageable);
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Post> findByMemberOrderByCreatedAtDescPostIdDesc(Member member, Pageable pageable);
    Page<Post> findByTeamOrderByCreatedAtDescPostIdDesc(Team team, Pageable pageable);
    Page<Post> findByProjectAndMemberOrderByCreatedAtDescPostIdDesc(Project project, Member member, Pageable pageable);
    Page<Post> findByAuthorsList_MemberOrderByCreatedAtDescPostIdDesc(Member member, Pageable pageable);
    Page<Post> findByProjectAndAuthorsList_MemberOrderByCreatedAtDescPostIdDesc(Project project, Member member, Pageable pageable);
    Page<Post> findByProjectAndTeamOrderByCreatedAtDescPostIdDesc(Project project, Team team, Pageable pageable);
    Page<Post> findByTeamAndMemberOrderByCreatedAtDescPostIdDesc(Team team, Member member, Pageable pageable);
    Page<Post> findByTeamAndAuthorsList_MemberOrderByCreatedAtDescPostIdDesc(Team team, Member member, Pageable pageable);

    boolean existsByTeam(Team team);
    boolean existsByProject(Project project);
    boolean existsByMember(Member member);

    Optional<Post> findTopByPostIdLessThanOrderByCreatedAtDescPostIdDesc(Long postId);
    Optional<Post> findTopByPostIdGreaterThanOrderByCreatedAtAscPostIdAsc(Long postId);

    @Query("SELECT p.postId FROM Post p JOIN p.categoriesList c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))")
    List<Long> findPostIdsByCategoryName(@Param("categoryName") String categoryName);
    Page<Post> findByPostIdIn(List<Long> postIds, Pageable pageable);

}
