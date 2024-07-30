package com.codiary.backend.global.service.MemberService;

import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.repository.PostRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@Getter
public class MemberQueryServiceImpl implements MemberQueryService{

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public Page<Post> getMyPosts(Member member, Pageable pageable) {
        Page<Post> posts = postRepository.findPostsByMemberOrAuthor(member, pageable);

        posts.getContent().forEach(post -> { //proxy 초기화
            post.getAuthorsList().size();
        });

        return posts;
    }

}
