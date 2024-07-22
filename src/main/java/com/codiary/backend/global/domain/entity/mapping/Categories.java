package com.codiary.backend.global.domain.entity.mapping;

import com.codiary.backend.global.domain.entity.Diary;
import com.codiary.backend.global.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Categories {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id", nullable = false, columnDefinition = "bigint")
  private Long categoryId;

  @Column(name = "name", nullable = false, columnDefinition = "varchar(500)")
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Diary diary;
}
