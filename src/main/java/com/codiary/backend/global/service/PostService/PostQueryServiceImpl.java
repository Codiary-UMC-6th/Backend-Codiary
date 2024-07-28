package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.repository.PostRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@Getter
public class PostQueryServiceImpl implements PostQueryService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public List<Post> findAllBySearch(Optional<String> optSearch) {
        // 만약 검색어가 존재한다면
        if (optSearch.isPresent()) {
            String search = optSearch.get();

            return postRepository.findAllByPostTitleContainingIgnoreCaseOrderByCreatedAtDesc(search);
        }
        // 검색어 존재 X
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Post> getMemberPost(Long memberId) {
        Member getMember = memberRepository.findById(memberId).get();
        List<Post> MemberPostList = postRepository.findAllByMember(getMember);

        return MemberPostList;
    }

    @Override
    public List<Post> getPostsByMonth(Long memberId, YearMonth yearMonth) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        List<Post> posts = postRepository.findByMemberAndCreatedAtBetweenOrderByCreatedAtAsc(member, startDate, endDate);

        return posts;
    }
}
