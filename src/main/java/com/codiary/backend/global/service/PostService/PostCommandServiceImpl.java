package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.converter.PostConverter;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.repository.PostRepository;
import com.codiary.backend.global.repository.TeamRepository;
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
    private final TeamRepository teamRepository; // 추가

    @Override
    public Post createPost(Long memberId, Long teamId, PostRequestDTO.CreatePostRequestDTO request) {
        // DTO를 Post 객체로 변환
        Post newPost = PostConverter.toPost(request);

        // Member와 Team을 가져오기
        Member getMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Team getTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        // Post 객체에 Member와 Team 설정
        newPost.setMember(getMember);
        newPost.setTeam(getTeam);

        // 저장
        Post savedPost = postRepository.save(newPost);

        return savedPost;
    }





}
