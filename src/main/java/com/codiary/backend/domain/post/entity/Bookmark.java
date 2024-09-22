package com.codiary.backend.domain.post.entity;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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

}
