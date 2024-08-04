package com.codiary.backend.global.service.MemberService;

import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.domain.entity.Bookmark;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.repository.BookmarkRepository;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
@Transactional
@Slf4j
@Getter
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;



    @Override
    public Page<Post> getMyPosts(Member member, String category, Pageable pageable) {
        Page<Post> posts = postRepository.findPostsByMemberOrAuthorAndCategoryName(member, category, pageable);

        posts.getContent().forEach(post -> { //proxy 초기화
            post.getAuthorsList().size();
            post.getCategoriesList().size();
            post.getPostFileList().size();
        });

        return posts;
    }

  
  
    // 회원별 북마크 리스트 조회
    @Override
    public Page<Bookmark> getBookmarkList(Long memberId, Integer page) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Page<Bookmark> memberPage = bookmarkRepository.findAllByMember(member, PageRequest.of(page - 1, 9));

        return memberPage;

    }

}
