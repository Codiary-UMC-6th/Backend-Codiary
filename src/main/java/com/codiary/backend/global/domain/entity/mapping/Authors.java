package com.codiary.backend.global.domain.entity.mapping;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authors {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "coAuthorId", nullable = false,columnDefinition = "bigint")
  private Long coAuthorId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

}
