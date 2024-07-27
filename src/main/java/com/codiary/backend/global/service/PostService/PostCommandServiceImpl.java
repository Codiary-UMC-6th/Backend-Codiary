package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.converter.PostConverter;
import com.codiary.backend.global.converter.PostFileConverter;
import com.codiary.backend.global.domain.entity.*;
import com.codiary.backend.global.domain.entity.mapping.Authors;
import com.codiary.backend.global.repository.*;
import com.codiary.backend.global.s3.AmazonS3Manager;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository; // 추가
    private final AmazonS3Manager s3Manager; // 추가
    private final UuidRepository uuidRepository; // 추가
    private final PostFileRepository postFileRepository; // 추가

    @Override
    public Post createPost(Long memberId, Long teamId, PostRequestDTO.CreatePostRequestDTO request) {

        Post newPost = PostConverter.toPost(request);

        Member getMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Team getTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        newPost.setMember(getMember);
        newPost.setTeam(getTeam);

        Post tempPost = postRepository.save(newPost);
        tempPost.setPostFileList(new ArrayList<>());

        if (request.getPostFiles() != null) {
            for (MultipartFile file : request.getPostFiles()) {
                String uuid = UUID.randomUUID().toString();
                Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
                String fileUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedUuid), file);

                PostFile newPostFile = PostFileConverter.toPostFile(fileUrl, newPost);
                postFileRepository.save(newPostFile);

                tempPost.getPostFileList().add(newPostFile);
            }
        }

        Post savedPost = postRepository.save(tempPost);
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

    @Override
    public Post setPostTeam(Long postId, Long teamId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        post.setTeam(team);

        return postRepository.save(post);
    }
}
