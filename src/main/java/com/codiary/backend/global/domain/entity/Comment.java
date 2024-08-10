package com.codiary.backend.global.domain.entity;

import com.codiary.backend.global.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false, columnDefinition = "bigint")
    private Long commentId;

    @Column(name = "comment_body", nullable = false, columnDefinition = "varchar(500)")
    private String commentBody;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentId;

    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();



    public void setMember(Member member) {
        if (this.member != null) {
            member.getCommentList().remove(this);
        }

        this.member = member;

        member.getCommentList().add(this);
    }

    public void setPost(Post post) {
        if (this.post != null) {
            post.getCommentList().remove(this);
        }

        this.post = post;

        post.getCommentList().add(this);
    }

    public void setParentId(Comment parentId) {
        if (this.parentId != null) {
            parentId.getChildComments().remove(this);
        }

        this.parentId = parentId;

        parentId.getChildComments().add(this);

    }

    // 댓글 수정하기
    public void patchComment(String commentBody) {
        this.commentBody = commentBody;
    }

}
