package com.codiary.backend.domain.comment.service;

import com.codiary.backend.domain.comment.dto.request.CommentRequestDTO;
import com.codiary.backend.domain.comment.entity.Comment;
import com.codiary.backend.domain.comment.repository.CommentRepository;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.repository.PostRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.codiary.backend.global.apiPayload.exception.handler.PostHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public Comment commentOnPost(Long postId, Long commenterId, CommentRequestDTO.CreateCommentDTO request) {
        // validation: 사용자, post 유무 확인
        // + 사용자가 해당 게시물에 대한 댓글 권한 있는지( 이후 구현 )
        Member commenter = memberRepository.findById(commenterId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        // business logic: 댓글 생성
        Comment comment = Comment.builder()
                .commentBody(request.commentBody())
                .member(commenter)
                .post(post)
                .build();

        // response: 댓글 반환
        return commentRepository.save(comment);
    }

    public String deleteComment(Long commentId, Long memberId) {
        // validation: 사용자, comment 유무 확인
        // + 사용자가 해당 댓글에 대한 댓글 권한 있는지( 이후 구현 )
        Member commenter = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        // business logic: 댓글 삭제
        commentRepository.delete(comment);

        // response: 삭제 성공 반환
        return "성공적으로 삭제되었습니다!";
    }

    @Transactional(readOnly = true)
    public List<Comment> getComments(Long postId, Long memberId, Pageable pageable) {
        // validation: 사용자, post 유무 확인
        // + 사용자가 해당 게시물에 대한 읽기 권한 있는지( 이후 구현 )
        Member commenter = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        // business logic: 댓글 조회
        List<Comment> comments = commentRepository.findByPostWithMemberInfoOrderByCreatedAtDesc(postId, pageable);

        // response: comment list 반환
        return comments;
    }
}
