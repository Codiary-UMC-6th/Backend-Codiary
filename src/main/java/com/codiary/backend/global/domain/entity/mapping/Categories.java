package com.codiary.backend.global.domain.entity.mapping;

import com.codiary.backend.global.domain.common.BaseEntity;
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

  @Column(name = "name", nullable = false, columnDefinition = "varchar(100)")
  private String name;

  @ManyToMany(mappedBy = "categoriesList")
  private List<Post> posts = new ArrayList<>();

  @Builder
  public Categories(String name) { this.name = name;}


}
