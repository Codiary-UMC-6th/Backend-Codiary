package com.codiary.backend.global.service.MemberService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Bookmark;
import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberQueryService {

    Page<Post> getMyPosts(Member member, String category, Pageable pageable);

    // 회원별 북마크 리스트 조회
    Page<Bookmark> getBookmarkList(Long memberId, Integer page);

    Member getUserProfile(Long userId);

    ApiResponse<MemberResponseDTO.MemberImageDTO> getProfileImage(Long memberId);

}
