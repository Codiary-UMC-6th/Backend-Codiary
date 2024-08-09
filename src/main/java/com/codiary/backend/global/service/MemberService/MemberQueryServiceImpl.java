package com.codiary.backend.global.service.MemberService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.codiary.backend.global.domain.entity.Bookmark;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.repository.BookmarkRepository;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.repository.PostRepository;
import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<Post> getMyPosts(Long memberId, Long projectId, Pageable pageable) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Page<Post> posts = postRepository.findPostsByMemberOrAuthorAndProjectId(member, projectId, pageable);

        posts.getContent().forEach(post -> { //proxy 초기화
            post.getAuthorsList().size();
            post.getProject();
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

    @Override
    public Member getUserProfile(Long userId) {
        Member user = memberRepository.findMemberWithTechStacks(userId);
        user = memberRepository.findMemberWithTeam(userId);
        if (user == null) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        return user;
    }

    @Override
    public ApiResponse<MemberResponseDTO.MemberImageDTO> getProfileImage(Long memberId) {
        Member user = memberRepository.findById(memberId).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        String url = (user.getImage() != null) ? user.getImage().getImageUrl() : "";
        MemberResponseDTO.MemberImageDTO response = new MemberResponseDTO.MemberImageDTO(url);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, response);
    }

}
