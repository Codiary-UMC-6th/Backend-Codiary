package com.codiary.backend.domain.coauthor.entity;

import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authors {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "coAuthorId", nullable = false,columnDefinition = "bigint")
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

  public static Authors createAuthors(Post post, Member member) {
    Authors authors = new Authors();
    authors.setPost(post);
    authors.setMember(member);
    return authors;
  }

}
