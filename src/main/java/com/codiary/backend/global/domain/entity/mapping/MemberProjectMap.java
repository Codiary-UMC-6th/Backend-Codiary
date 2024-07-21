package com.codiary.backend.global.domain.entity.mapping;

import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Project;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberProjectMap {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberProjectId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  private Project project;

}
