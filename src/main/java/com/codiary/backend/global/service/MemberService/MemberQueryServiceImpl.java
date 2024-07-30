package com.codiary.backend.global.service.MemberService;

import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.domain.entity.Bookmark;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.repository.BookmarkRepository;
import com.codiary.backend.global.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;

    // 회원별 북마크 리스트 조회
    @Override
    public Page<Bookmark> getBookmarkList(Long memberId, Integer page) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Page<Bookmark> memberPage = bookmarkRepository.findAllByMember(member, PageRequest.of(page - 1, 9));

        return memberPage;

    }

}
