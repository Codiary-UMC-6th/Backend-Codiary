package com.codiary.backend.domain.post.repository;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.entity.Bookmark;
import com.codiary.backend.domain.post.entity.Post;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Boolean existsByMemberAndPost(Member member, Post post);

    Optional<Bookmark> findByMemberAndPost(Member member, Post post);

    @Query("SELECT b.post.postId FROM Bookmark b WHERE b.member.memberId = :memberId")
    List<Long> findBookmarkedPostIdsByMemberId(@Param("memberId") Long memberId);



}
