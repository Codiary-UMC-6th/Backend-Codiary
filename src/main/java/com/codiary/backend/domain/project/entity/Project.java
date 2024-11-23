package com.codiary.backend.domain.project.entity;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE project SET deleted_at = NOW() WHERE project_id = ?")
public class Project extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "project_id", nullable = false, columnDefinition = "bigint")
  private Long projectId;

  @Column(name="project_name", nullable = false, columnDefinition = "varchar(256)")
  private String projectName;

  @OneToMany(mappedBy = "project")
  private List<Post> posts = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Builder
  public Project(Team team, Member member, String projectName) {
    this.team = team;
    this.member = member;
    this.projectName = projectName;
  }
}
