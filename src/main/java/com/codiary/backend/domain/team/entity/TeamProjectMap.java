package com.codiary.backend.domain.team.entity;

import com.codiary.backend.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamProjectMap {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "team_project__id", nullable = false, columnDefinition = "bigint")
  private Long teamProjectId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  private Project project;
}
