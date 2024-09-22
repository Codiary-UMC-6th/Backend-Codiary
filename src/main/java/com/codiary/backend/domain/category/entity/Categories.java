package com.codiary.backend.domain.category.entity;

import com.codiary.backend.domain.member.entity.MemberCategory;
import com.codiary.backend.domain.post.entity.Post;
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

  @Column(nullable = false, columnDefinition = "varchar(100)")
  private String name;

  @ManyToMany(mappedBy = "categoriesList")
  private List<Post> posts = new ArrayList<>();

  @OneToMany(mappedBy = "categories", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MemberCategory> memberCategoryList = new ArrayList<>();
}
