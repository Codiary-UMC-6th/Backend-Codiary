package com.codiary.backend.global.service.BookmarkService;

import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.converter.BookmarkConverter;
import com.codiary.backend.global.domain.entity.Bookmark;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.repository.BookmarkRepository;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookmarkCommandServiceImpl implements BookmarkCommandService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // 스프링 시큐리티 완료되면 전체적으로 로그인 회원 관련 수정해야 함

    // 북마크 추가
    @Override
    public Bookmark createBookmark(Long memberId, Long postId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Bookmark bookmark = BookmarkConverter.toBookmark(member, post);

        return bookmarkRepository.save(bookmark);

    }

    // 북마크 삭제
    @Override
    public void deleteBookmark(Long bookmarkId) {

        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmark);

    }

    // 게시글의 북마크 개수 조회
    @Override
    public int countBookmark(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        return bookmarkRepository.countByPost(post);

    }

}
