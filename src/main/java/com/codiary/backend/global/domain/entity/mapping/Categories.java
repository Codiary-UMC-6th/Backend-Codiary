package com.codiary.backend.global.domain.entity.mapping;

import com.codiary.backend.global.domain.entity.Diary;
import com.codiary.backend.global.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Categories {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long categoryId;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Diary diary;
}
