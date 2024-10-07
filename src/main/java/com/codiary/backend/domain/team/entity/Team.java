package com.codiary.backend.domain.team.entity;

import com.codiary.backend.domain.post.entity.Post;
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

  @Builder
    public Team(Long teamId, String name, String intro, String github, String email, String linkedin, String discord, String instagram, List<TeamProjectMap> teamProjectMapList, List<Post> postList, List<TeamMember> teamMemberList, List<TeamFollow> followers, TeamBannerImage bannerImage, TeamProfileImage profileImage) {
        this.teamId = teamId;
        this.name = name;
        this.intro = intro;
        this.github = github;
        this.email = email;
        this.linkedin = linkedin;
        this.discord = discord;
        this.instagram = instagram;
        this.teamProjectMapList = teamProjectMapList;
        this.postList = postList;
        this.teamMemberList = teamMemberList;
        this.followers = followers;
        this.bannerImage = bannerImage;
        this.profileImage = profileImage;
    }
}
