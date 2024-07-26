package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.converter.PostConverter;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.domain.entity.mapping.Authors;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.repository.PostRepository;
import com.codiary.backend.global.repository.TeamRepository;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

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

        Post newPost = PostConverter.toPost(request);

        Member getMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Team getTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        newPost.setMember(getMember);
        newPost.setTeam(getTeam);

        Post savedPost = postRepository.save(newPost);

        return savedPost;
    }

    @Override
    public Post updatePost(Long memberId, Long postId, PostRequestDTO.UpdatePostDTO request) {
        Member getMember = memberRepository.findById(memberId).get();

        Post updatePost = postRepository.findById(postId).get();
        updatePost.update(request);

        return updatePost;
    }

    @Override
    public void deletePost(Long memberId, Long postId) {
        Member getMember = memberRepository.findById(memberId).get();

        Post deletePost = postRepository.findById(postId).get();
        postRepository.delete(deletePost);
    }


    @Override
    public Post updateVisibility(Long postId, PostRequestDTO.UpdateVisibilityRequestDTO request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        post.setPostStatus(request.getPostStatus());
        return postRepository.save(post);
    }

    @Override
    public Post updateCoauthors(Long postId, PostRequestDTO.UpdateCoauthorRequestDTO request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 기존 공동 저자 리스트 삭제
        post.getAuthorsList().clear();

        // 새로운 공동 저자 리스트 추가
        Set<Authors> coauthors = request.getMemberIds().stream()
                .map(memberId -> {
                    Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
                    return Authors.createAuthors(post, member);
                }).collect(Collectors.toSet());

        post.getAuthorsList().addAll(coauthors);

        return postRepository.save(post);
    }

}
