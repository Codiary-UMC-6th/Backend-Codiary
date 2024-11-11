package com.codiary.backend.domain.post.service;




import com.codiary.backend.domain.category.entity.Category;
import com.codiary.backend.domain.category.service.CategoryService;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;

import com.codiary.backend.domain.member.service.MemberCommandService;
import com.codiary.backend.domain.post.converter.PostConverter;
import com.codiary.backend.domain.post.converter.PostFileConverter;
import com.codiary.backend.domain.post.dto.request.PostRequestDTO;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.entity.PostFile;
import com.codiary.backend.domain.post.repository.PostFileRepository;
import com.codiary.backend.domain.post.repository.PostRepository;
import com.codiary.backend.domain.project.repository.ProjectRepository;
import com.codiary.backend.domain.team.repository.TeamRepository;
import com.codiary.backend.global.common.uuid.Uuid;
import com.codiary.backend.global.common.uuid.UuidRepository;
import com.codiary.backend.global.s3.AmazonS3Manager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostCommandService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;
    private final UuidRepository uuidRepository; // 추가
    private final PostFileRepository postFileRepository;
    private final MemberCommandService memberCommandService;
    private final CategoryService categoryService;
    private final AmazonS3Manager s3Manager;

    // 포스트 생성
    public Post createPost(PostRequestDTO.CreatePostRequestDTO request) {

        Post newPost = PostConverter.toPost(request, teamRepository, projectRepository);
        Member getMember = memberCommandService.getRequester();

        newPost.setMember(getMember);

        Post tempPost = postRepository.save(newPost);
        tempPost.setPostFileList(new ArrayList<>());

        if (request.getPostFiles() != null) {
            for (MultipartFile file : request.getPostFiles()) {
                if (file.isEmpty()) {
                    continue;
                }
                String uuid = UUID.randomUUID().toString();
                Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
                String fileUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedUuid), file);

                PostFile newPostFile = PostFileConverter.toPostFile(fileUrl, newPost, file.getOriginalFilename());
                postFileRepository.save(newPostFile);

                tempPost.getPostFileList().add(newPostFile);
            }
        }

        // 대표 사진 설정
        String thumbnailImageName = request.getThumbnailImageName();
        for (PostFile postFile : tempPost.getPostFileList()) {
            if (postFile.getFileName().equals(thumbnailImageName)) {
                tempPost.setThumbnailImage(postFile);
            }
        }
        if (tempPost.getPostFileList().size() != 0 && tempPost.getThumbnailImage() == null) {
            tempPost.setThumbnailImage(tempPost.getPostFileList().get(0));
        }

        Post savedPost = postRepository.save(tempPost);
        return savedPost;
    }


    public Post updatePost(Long postId, PostRequestDTO.UpdatePostDTO request) {
        Member getMember = memberCommandService.getRequester();
        Post updatePost = postRepository.findById(postId).get();
        updatePost.update(request);

        // 새로운 이미지 추가
        if (request.getAddedPostFiles() != null) {
            for (MultipartFile file : request.getAddedPostFiles()) {
                if (file.isEmpty()) {
                    continue;
                }
                String uuid = UUID.randomUUID().toString();
                Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
                String fileUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedUuid), file);

                PostFile newPostFile = PostFileConverter.toPostFile(fileUrl, updatePost, file.getOriginalFilename());
                postFileRepository.save(newPostFile);

                updatePost.getPostFileList().add(newPostFile);
            }
        }

        // 대표 사진 설정
        String thumbnailImageName = request.getThumbnailImageName();
        for (PostFile postFile : updatePost.getPostFileList()) {
            if (postFile.getFileName() == thumbnailImageName) {
                updatePost.setThumbnailImage(postFile);
            }
        }
        if (updatePost.getPostFileList().size() != 0 && updatePost.getThumbnailImage() == null) {
            updatePost.setThumbnailImage(updatePost.getPostFileList().get(0));
        }

        return postRepository.save(updatePost);
    }


    public void deletePost(Long postId) {
        Member getMember = memberCommandService.getRequester();

        Post deletePost = postRepository.findById(postId).get();
        postRepository.delete(deletePost);
    }


    public Post setPostCategories(Long postId, Set<String> categoryNames) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 카테고리 이름으로 Categories 엔티티를 생성하거나 조회
        List<Category> categories = categoryNames.stream()
                .map(name -> {
                    // 카테고리 이름으로 Categories 엔티티를 조회하거나 새로 생성
                    return categoryService.addCategory(post, name);
                })
                .collect(Collectors.toList());

        // 포스트에 카테고리를 설정
        post.setCategories(categories);

        return postRepository.save(post);
    }

}
