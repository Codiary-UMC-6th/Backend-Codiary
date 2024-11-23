package com.codiary.backend.domain.coauthor.entity;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.entity.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Author {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "co_author_id", nullable = false,columnDefinition = "bigint")
  private Long coAuthorId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  public void setPost(Post post) {
    this.post = post;
  }

  public void setMember(Member member) {
    this.member = member;
  }
}
