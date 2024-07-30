package com.codiary.backend.global.service.MemberService;

import com.codiary.backend.global.domain.entity.Bookmark;
import org.springframework.data.domain.Page;

public interface MemberQueryService {

    Page<Bookmark> getBookmarkList(Long memberId, Integer page);

}
