package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.converter.PostConverter;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.repository.PostRepository;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostCommandServiceImpl implements PostCommandService{

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    public Post createPost(Long memberId, PostRequestDTO.CreatePostRequestDTO request) {
        Post newPost = PostConverter.toPost(request);
        Member getMember = memberRepository.findById(memberId).get();
        newPost.setMember(getMember);

        Post savedPost = postRepository.save(newPost);

        return savedPost;
    }





}
