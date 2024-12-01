package com.codiary.backend.domain.comment.service;

import com.codiary.backend.domain.comment.dto.request.CommentRequestDTO;
import com.codiary.backend.domain.comment.dto.request.CommentRequestDTO.CommentDTO;
import com.codiary.backend.domain.comment.entity.Comment;
import com.codiary.backend.domain.comment.repository.CommentRepository;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.member.security.CustomMemberDetails;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.codiary.backend.domain.post.repository.PostRepository;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.repository.TeamRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.codiary.backend.global.apiPayload.exception.handler.PostHandler;
import com.codiary.backend.global.apiPayload.exception.handler.TeamHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final TeamRepository teamRepository;

    public Comment commentOnPost(Long postId, Long commenterId, CommentDTO request) {
        // validation: 사용자, post 유무 확인
        Member commenter = memberRepository.findById(commenterId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        // validation: 사용자가 해당 게시물에 대한 권한 있는지
        if (post.getPostAccess().equals(PostAccess.MEMBER) && post.getMember() != commenter) {
            throw new GeneralException(ErrorStatus.COMMENT_CREATE_UNAUTHORIZED);
        } else if (post.getPostAccess().equals(PostAccess.TEAM)) {
            Team teamOfPost = teamRepository.findByIdWithTeamMemberList(post.getTeam().getTeamId())
                    .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
            if (!teamRepository.isTeamMember(teamOfPost, commenter)) {
                throw new GeneralException((ErrorStatus.COMMENT_CREATE_UNAUTHORIZED));
            }
        }

        // business logic: 댓글 생성
        Comment comment = Comment.builder()
                .commentBody(request.commentBody())
                .member(commenter)
                .post(post)
                .parent(null)
                .build();

        // response: 댓글 반환
        return commentRepository.save(comment);
    }

    public Comment replyToComment(Long commentId, Long replierId, CommentRequestDTO.CommentDTO request) {
        // validation: 사용자 유무, 댓글 유무, 대댓글에 댓글 방지
        Member replier = memberRepository.findById(replierId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));
        if (comment.getPost() == null) {
            throw new GeneralException(ErrorStatus.COMMENT_REPLY_OF_REPLY_NOT_ALLOWED);
        }
        Post post = postRepository.findById(comment.getPost().getPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        // validation: 사용자 권한 확인
        if (post.getPostAccess().equals(PostAccess.MEMBER) && post.getMember() != replier) {
            throw new GeneralException(ErrorStatus.COMMENT_CREATE_UNAUTHORIZED);
        } else if (post.getPostAccess().equals(PostAccess.TEAM)) {
            Team teamOfPost = teamRepository.findByIdWithTeamMemberList(post.getTeam().getTeamId())
                    .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
            if (!teamRepository.isTeamMember(teamOfPost, replier)) {
                throw new GeneralException((ErrorStatus.COMMENT_CREATE_UNAUTHORIZED));
            }
        }

        // business logic: 대댓글 생성
        Comment reply = Comment.builder()
                .commentBody(request.commentBody())
                .member(replier)
                .parent(comment)
                .post(null)
                .build();

        // response: 대댓글 반환
        return commentRepository.save(reply);
    }

    public String deleteComment(Long commentId, Long memberId) {
        // validation: 사용자, comment 유무 확인
        // + 사용자가 해당 댓글에 대한 권한 있는지
        Member requester = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));
        if (comment.getMember() != requester) {
            throw new GeneralException(ErrorStatus.COMMENT_DELETE_UNAUTHORIZED);
        }

        // business logic: 댓글 삭제
        commentRepository.delete(comment);

        // response: 삭제 성공 반환
        return "성공적으로 삭제되었습니다!";
    }

    public Comment updateComment(Long commentId, Long memberId, CommentRequestDTO.CommentDTO request) {
        // validation: 사용자, comment 유무 확인
        // + 사용자가 해당 댓글에 대한 권한 있는지
        Member requester = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Comment comment = commentRepository.findByIdWithReplies(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));
        if (comment.getMember() != requester) {
            throw new GeneralException(ErrorStatus.COMMENT_UPDATE_UNAUTHORIZED);
        }

        // business logic: 댓글 수정
        comment.setCommentBody(request.commentBody());

        // response
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Page<Comment> getComments(Long postId, CustomMemberDetails memberDetails, Pageable pageable) {
        // validation: post 유무 확인
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        // validation: 사용자가 해당 게시물에 대한 권한 있는지
        if (!post.getPostAccess().equals(PostAccess.ENTIRE)) {
            if (memberDetails == null) {
                throw new GeneralException(ErrorStatus.COMMENT_READ_UNAUTHORIZED);
            }

            Member requester = memberRepository.findById(memberDetails.getId())
                    .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

            if (post.getPostAccess().equals(PostAccess.MEMBER) && post.getMember() != requester) {
                throw new GeneralException(ErrorStatus.COMMENT_READ_UNAUTHORIZED);
            } else {
                Team teamOfPost = teamRepository.findByIdWithTeamMemberList(post.getTeam().getTeamId())
                        .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
                if (!teamRepository.isTeamMember(teamOfPost, requester)) {
                    throw new GeneralException((ErrorStatus.COMMENT_READ_UNAUTHORIZED));
                }
            }
        }

        // business logic: 댓글 조회
        Page<Comment> comments
                = commentRepository.findByPostWithMemberInfoAndRepliesOrderByCreatedAtAsc(postId, pageable);

        // response: comment list 반환
        return comments;
    }

    @Transactional(readOnly = true)
    public Page<Comment> getReplies(Long commentId, CustomMemberDetails memberDetails, Pageable pageable) {
        // validation: 댓글 유무 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        // validation: 사용자가 해당 게시물에 대한 권한 있는지
        Post post = postRepository.findById(comment.getPost().getPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));
        if (!post.getPostAccess().equals(PostAccess.ENTIRE)) {
            if (memberDetails == null) {
                throw new GeneralException(ErrorStatus.COMMENT_READ_UNAUTHORIZED);
            }
            
            Member requester = memberRepository.findById(memberDetails.getId())
                    .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

            if (post.getPostAccess().equals(PostAccess.MEMBER) && post.getMember() != requester) {
                throw new GeneralException(ErrorStatus.COMMENT_READ_UNAUTHORIZED);
            } else {
                Team teamOfPost = teamRepository.findByIdWithTeamMemberList(post.getTeam().getTeamId())
                        .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
                if (!teamRepository.isTeamMember(teamOfPost, requester)) {
                    throw new GeneralException((ErrorStatus.COMMENT_READ_UNAUTHORIZED));
                }
            }
        }

        // business logic: 대댓글 조회
        Page<Comment> replies = commentRepository.findByParentWithMemberInfoOrderByCreatedAtAsc(commentId, pageable);

        // response
        return replies;
    }
}
