package com.codiary.backend.domain.member.entity;

import com.codiary.backend.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProjectMap {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_project_id", nullable = false, columnDefinition = "bigint")
  private Long memberProjectId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  private Project project;
}
