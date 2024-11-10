package com.codiary.backend.domain.post.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.team.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Page<Post> findByMemberOrderByCreatedAtDescPostIdDesc(Member member, Pageable pageable);
    Page<Post> findByTeamOrderByCreatedAtDescPostIdDesc(Team team, Pageable pageable);
    Page<Post> findByAuthorsList_MemberOrderByCreatedAtDescPostIdDesc(Member member, Pageable pageable);

    boolean existsByTeam(Team team);

}
