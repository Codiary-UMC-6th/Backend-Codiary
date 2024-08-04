package com.codiary.backend.global.service.MemberService;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Bookmark;
import com.codiary.backend.global.domain.entity.mapping.MemberCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberQueryService {

    Page<Post> getMyPosts(Member member, Pageable pageable);

    // 회원별 북마크 리스트 조회
    Page<Bookmark> getBookmarkList(Long memberId, Integer page);

}
