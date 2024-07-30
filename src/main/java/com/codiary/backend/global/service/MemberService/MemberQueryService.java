package com.codiary.backend.global.service.MemberService;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberQueryService {

    Page<Post> getMyPosts(Member member, Pageable pageable);
    
    Page<Bookmark> getBookmarkList(Long memberId, Integer page);

}
