package com.codiary.backend.domain.post.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.team.entity.Team;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> searchPost(Long memberId, String keyword, Pageable pageable);

    Page<Post> getLatestPostsOfFollowings(Long memberId, Pageable pageable);

    Page<Post> getPostList(Pageable pageable);

    Page<Post> getPostsByCategoryId(Long memberId, Long categoryId, Pageable pageable);

    Map<Project, List<Post>> findPostsForCalendar(Long memberId, LocalDate date);

    Page<Post> findPostsByFollowing(Long id, Pageable pageable);
    
    Page<Post> findByBookmarkPostList(Member member, Pageable pageable);
    
    Page<Post> getPostsByName(Long memberId, String authorName, String teamName, String projectName, Pageable pageable);

    Optional<Post> findByIdWithTeam(Long postId, Long requesterId);

    Page<Post> findAllByPostTitleContainingIgnoreCaseOrderByCreatedAtDesc(String postTitle, Pageable pageable);

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Optional<Post> findTopByTeamAndPostIdLessThanOrderByCreatedAtDescPostIdDesc(Team team, Long postId);

    Optional<Post> findTopByTeamAndPostIdGreaterThanOrderByCreatedAtAscPostIdAsc(Team team, Long postId);

    Optional<Post> findTopByMemberAndPostIdLessThanOrderByCreatedAtDescPostIdDesc(Member member, Long postId);

    Optional<Post> findTopByMemberAndPostIdGreaterThanOrderByCreatedAtAscPostIdAsc(Member member, Long postId);
}
