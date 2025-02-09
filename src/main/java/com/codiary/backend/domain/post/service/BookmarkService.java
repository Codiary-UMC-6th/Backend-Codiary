package com.codiary.backend.domain.post.service;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.post.entity.Bookmark;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.codiary.backend.domain.post.repository.BookmarkRepository;
import com.codiary.backend.domain.post.repository.PostRepository;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.repository.TeamRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.apiPayload.exception.handler.BookmarkHandler;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.codiary.backend.global.apiPayload.exception.handler.PostHandler;
import com.codiary.backend.global.apiPayload.exception.handler.TeamHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final BookmarkRepository bookmarkRepository;
    private final TeamRepository teamRepository;

    public Bookmark bookmarkPost(Long memberId, Long postId) {
        // validation: member, post 여부
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        // validation: 이미 북마크한 상태
        if (bookmarkRepository.existsByMemberAndPost(member, post)) {
            throw new BookmarkHandler(ErrorStatus.BOOKMARK_ALREADY_EXIST);
        }

        // validation: post 권한 확인
        if (post.getPostAccess().equals(PostAccess.MEMBER) && post.getMember() != member) {
            throw new GeneralException(ErrorStatus.BOOKMARK_CREATE_UNAUTHORIZED);
        } else if (post.getPostAccess().equals(PostAccess.TEAM)) {
            Team teamOfPost = teamRepository.findByIdWithTeamMemberList(post.getTeam().getTeamId())
                    .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
            if (!teamRepository.isTeamMember(teamOfPost, member)) {
                throw new GeneralException((ErrorStatus.BOOKMARK_CREATE_UNAUTHORIZED));
            }
        }

        // business logic
        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .post(post)
                .build();
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        // response
        return savedBookmark;
    }

    public String cancelBookmark(Long memberId, Long postId) {
        // validation: member, post, bookmark 여부
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        Bookmark bookmark = bookmarkRepository.findByMemberAndPost(member, post)
                .orElseThrow(() -> new BookmarkHandler(ErrorStatus.BOOKMARK_ALREADY_CANCELED));

        // business logic
        member.getBookmarkList().remove(bookmark);
        post.getBookmarkList().remove(bookmark);
        bookmarkRepository.deleteById(bookmark.getId());

        // response
        return "북마크가 취소되었습니다.";
    }

    public List<Long> getBookmarkedPostIdsByMemberId(Long memberId) {
        return bookmarkRepository.findBookmarkedPostIdsByMemberId(memberId);
    }

    // 특정 게시글의 북마크 개수 조회
    public int getBookmarkCountByPostId(Long postId) {
        return bookmarkRepository.countByPostPostId(postId);
    }
}
