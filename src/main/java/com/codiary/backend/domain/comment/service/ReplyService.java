package com.codiary.backend.domain.comment.service;

import com.codiary.backend.domain.comment.dto.request.CommentRequestDTO;
import com.codiary.backend.domain.comment.entity.Comment;
import com.codiary.backend.domain.comment.repository.CommentRepository;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.codiary.backend.domain.post.repository.PostRepository;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.repository.TeamRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.apiPayload.exception.handler.TeamHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final TeamRepository teamRepository;

    public Comment replyToComment(Long commentId, Long replierId, CommentRequestDTO.CommentDTO request) {
        // validation: 사용자 유무, 댓글 유무
        Member replier = memberRepository.findById(replierId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));
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
        Comment reply = Comment.replyBuilder()
                .commentBody(request.commentBody())
                .member(replier)
                .comment(comment)
                .build();

        // response: 대댓글 반환
        return commentRepository.save(reply);
    }
}
