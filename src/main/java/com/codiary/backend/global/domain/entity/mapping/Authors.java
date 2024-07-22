package com.codiary.backend.global.domain.entity.mapping;

import com.codiary.backend.global.domain.entity.Diary;
import com.codiary.backend.global.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authors {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long coAuthorId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Diary diary;

}
