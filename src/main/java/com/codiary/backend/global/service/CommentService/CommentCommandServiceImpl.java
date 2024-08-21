package com.codiary.backend.global.service.CommentService;

import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.converter.CommentConverter;
import com.codiary.backend.global.domain.entity.Comment;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.repository.CommentRepository;
import com.codiary.backend.global.repository.PostRepository;
import com.codiary.backend.global.web.dto.Comment.CommentRequestDTO;
import com.codiary.backend.global.web.dto.Comment.CommentResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentCommandServiceImpl implements CommentCommandService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // 댓글 수정하기///////
    @Override
    public Comment patchComment(Long commentId, CommentRequestDTO.PatchCommentDTO request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        if (request != null) {
            comment.patchComment(request.getCommentBody());
        }

        CommentConverter.toPatchCommentResultDTO(comment);

        return commentRepository.save(comment);

    }

    // 댓글 삭제하기
    @Override
    public void deleteComment(Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        commentRepository.delete(comment);

    }

    // 게시글의 댓글 개수 조회
    @Override
    public int countComments(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        return commentRepository.countByPost(post);

    }


}
