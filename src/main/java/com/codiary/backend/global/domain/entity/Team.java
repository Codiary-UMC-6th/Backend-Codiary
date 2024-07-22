package com.codiary.backend.global.domain.entity;

import com.codiary.backend.global.domain.entity.mapping.TeamMember;
import com.codiary.backend.global.domain.entity.mapping.TeamProjectMap;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "team_id", nullable = false, columnDefinition = "bigint")
  private Long teamId;

  @Column(name = "name", nullable = false, columnDefinition = "varchar(256)")
  private String name;

  @Column(name = "intro", columnDefinition = "varchar(256)")
  private String intro;

  @Column(name = "profilePhoto", columnDefinition = "varchar(256)")
  private String profilePhoto;

  @Column(name = "github", columnDefinition = "varchar(256)")
  private String github;

  @Column(name = "email", columnDefinition = "varchar(256)")
  private String email;

  @Column(name = "linkedin", columnDefinition = "varchar(256)")
  private String linkedin;

  @Column(name = "instagram", columnDefinition = "varchar(256)")
  private String instagram;

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TeamProjectMap> teamProjectMapList = new ArrayList<>();

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> postList = new ArrayList<>();

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TeamMember> teamMemberList = new ArrayList<>();

}
