package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Bookmark;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    // 게시글의 북마크 개수 조회
    int countByPost(Post post);

    // 회원별 북마크 리스트 조회
    @Query("SELECT b FROM Bookmark b JOIN FETCH b.post p LEFT JOIN FETCH p.postPhotoList LEFT JOIN FETCH p.member WHERE b.member = :member")
    Page<Bookmark> findAllByMember(Member member, Pageable pageable);

}
