package com.codiary.backend.domain.comment.entity;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false, columnDefinition = "bigint")
    private Long commentId;

    @Column(nullable = false, columnDefinition = "varchar(500)")
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

    @Builder
    public Comment(String commentBody, Member member, Post post) {
        this.commentBody = commentBody;
        this.member = member;
        this.post = post;
    }

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

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }
}
