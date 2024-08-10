package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.domain.entity.Comment;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;
import com.codiary.backend.global.web.dto.Post.PostResponseDTO;

import java.util.List;
import java.util.Set;

public interface PostCommandService {

    // 포스트 생성
    Post createPost(PostRequestDTO.CreatePostRequestDTO request);
    //포스트 수정
    Post updatePost(Long postId, PostRequestDTO.UpdatePostDTO request);
    //포스트 삭제
    void deletePost(Long postId);
    // 공개/비공개 설정
    Post updateVisibility(Long postId, PostRequestDTO.UpdateVisibilityRequestDTO request);
    // 공동 저자 설정
    Post updateCoauthors(Long postId, PostRequestDTO.UpdateCoauthorRequestDTO request);
    // 글 소속 팀 설정
    Post setPostTeam(Long postId, Long teamId);

    Post setPostCategories(Long postId, Set<String> categories);

    // 게시글에 댓글 작성하기
    Comment createComment(Long memberId, Long postId, PostRequestDTO.CommentDTO request);

    // 게시글에 대댓글 작성하기
    Comment createCommentReply(Long memberId, Long postId, Long parentId, PostRequestDTO.CommentReplyDTO request);

    // 게시글별 댓글 조회
//    List<Comment> getCommentList(Long postId);
    List<PostResponseDTO.CommentDTO> getCommentList(Long postId);

}
