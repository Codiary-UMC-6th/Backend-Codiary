package com.codiary.backend.global.domain.entity.mapping;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Categories {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id", nullable = false, columnDefinition = "bigint")
  private Long categoryId;

  //우선 String으로 설정, Category 나오면 enum으로 변경 필요
  //@Enumerated(EnumType.STRING)
  @Column(name = "name", nullable = false, columnDefinition = "varchar(100)")
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  @Builder
  public Categories(Post post, String name, Member member) {
    this.post = post;
    this.name = name;
    this.member = member;
  }
  public Categories(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
  public static Categories createCategory(Post post, String name, Member member) {
    return Categories.builder()
            .post(post)
            .name(name)
            .member(member)
            .build();
  }

}
