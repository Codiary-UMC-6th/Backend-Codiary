package com.codiary.backend.domain.post.service;

import com.codiary.backend.domain.post.converter.PostConverter;
import com.codiary.backend.domain.post.converter.PostFileConverter;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.entity.Uuid;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.member.repository.UuidRepository;
import com.codiary.backend.domain.member.service.MemberCommandService;
import com.codiary.backend.domain.post.dto.request.PostRequestDTO;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.entity.PostFile;
import com.codiary.backend.domain.post.repository.PostRepository;
import com.codiary.backend.domain.post.repository.PostFileRepository;
import com.codiary.backend.domain.project.repository.ProjectRepository;
import com.codiary.backend.domain.team.repository.TeamRepository;
import com.codiary.backend.global.s3.AmazonS3Manager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.UUID;

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
}
