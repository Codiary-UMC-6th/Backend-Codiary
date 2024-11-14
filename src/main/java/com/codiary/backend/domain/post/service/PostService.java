package com.codiary.backend.domain.post.service;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.entity.MemberCategory;
import com.codiary.backend.domain.member.repository.MemberCategoryRepository;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.repository.PostRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MemberCategoryRepository memberCategoryRepository;

    public Page<Post> searchPost(Long memberId, String keyword, Pageable pageable) {
        //business logic & return
        return postRepository.searchPost(memberId, keyword, pageable);
    }

    // 인기글 조회
    public Page<Post> getPopularPosts(Pageable pageable) {
        // business logic & return
        return null;
    }

    // 카테고리 인기글 조회
    public Page<Post> getCategoryPopularPosts(Long memberId, Long memberCategoryId, Pageable pageable) {
        // validation: 멤버 존재하는지 & 멤버의 카테고리 맞는지
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        MemberCategory memberCategory
                = memberCategoryRepository.findByMemberCategoryIdAndMember(memberCategoryId, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND));

        // business logic & return
        return null;
    }

    // 최신글 조회
    public Page<Post> getLatestPosts(Pageable pageable) {
        // business logic & return
        return postRepository.findPostsWithAuthorInfoOrderByCreatedAtDesc(pageable);
    }

    // 팔로잉 멤버 게시글 조회
    public Page<Post> getFollowingMemberPosts(Long memberId, Pageable pageable) {
        // validation: 멤버 존재하는지
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // business logic: 다이어리 조회
        Page<Post> posts = postRepository.findPostsByMemberWithAuthorInfoOrderByDesc(member.getMemberId(), pageable);

        // business logic & return
        return posts;
    }
}
