package com.codiary.backend.global.domain.entity.mapping;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Categories {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id", nullable = false, columnDefinition = "bigint")
  private Long categoryId;

  //@Enumerated(EnumType.STRING)
  @Column(name = "name", nullable = false, columnDefinition = "varchar(100)")
  private String name;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "member_id")
//  private Member member;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "post_id")
//  private Post post;
  @ManyToMany(mappedBy = "categoriesList")
  private List<Post> posts = new ArrayList<>();

  @Builder
  public Categories(String name) { this.name = name;}
  public static Categories createCategory(Post post, String name, Member member) {
    Categories category = new Categories(name);
    category.getPosts().add(post);
    post.getCategoriesList().add(category);
    return category;
  }

}
