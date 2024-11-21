package com.codiary.backend.domain.post.entity;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public void setMember(Member member) {
        if (this.member != null) {
            member.getBookmarkList().remove(this);
        }

        this.member = member;

        member.getBookmarkList().add(this);
    }

    public void setPost(Post post) {
        if (this.post != null) {
            post.getBookmarkList().remove(this);
        }

        this.post = post;

        post.getBookmarkList().add(this);
    }

    @Builder
    public Bookmark(Member member, Post post) {
        this.member = member;
        this.post = post;
    }

}
