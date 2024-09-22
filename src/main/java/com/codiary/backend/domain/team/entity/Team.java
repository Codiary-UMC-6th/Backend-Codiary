package com.codiary.backend.domain.team.entity;

import com.codiary.backend.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Team {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "team_id", nullable = false, columnDefinition = "bigint")
  private Long teamId;

  @Column(name = "name", nullable = false, columnDefinition = "varchar(256)")
  private String name;

  @Column(name = "intro", columnDefinition = "varchar(256)")
  private String intro;

  @Column(name = "github", columnDefinition = "varchar(256)")
  private String github;

  @Column(name = "email", columnDefinition = "varchar(256)")
  private String email;

  @Column(name = "linkedin", columnDefinition = "varchar(256)")
  private String linkedin;

  @Column(name = "discord", columnDefinition = "varchar(256)")
  private String discord;

  @Column(name = "instagram", columnDefinition = "varchar(256)")
  private String instagram;

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TeamProjectMap> teamProjectMapList = new ArrayList<>();

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> postList = new ArrayList<>();

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TeamMember> teamMemberList = new ArrayList<>();

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TeamFollow> followers = new ArrayList<>();

  @OneToOne(mappedBy = "team", cascade = CascadeType.ALL)
  private TeamBannerImage bannerImage;

  @OneToOne(mappedBy = "team", cascade = CascadeType.ALL)
  private TeamProfileImage profileImage;
}
