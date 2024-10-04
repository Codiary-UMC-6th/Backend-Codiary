package com.codiary.backend.domain.member.entity;

import com.codiary.backend.domain.coauthor.entity.Authors;
import com.codiary.backend.domain.comment.entity.Comment;
import com.codiary.backend.domain.post.entity.Bookmark;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.team.entity.TeamFollow;
import com.codiary.backend.domain.team.entity.TeamMember;
import com.codiary.backend.domain.techstack.entity.TechStacks;
import com.codiary.backend.global.common.BaseEntity;
import com.codiary.backend.domain.member.enumerate.MemberState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id", nullable = false, columnDefinition = "bigint")
  private Long memberId;

  @Column(nullable = false, columnDefinition = "varchar(256)")
  private String email;

  @Column(nullable = false, columnDefinition = "varchar(256)")
  private String password;

  @Column(nullable = false, columnDefinition = "varchar(256)")
  private String nickname;

  @Column(columnDefinition = "varchar(256)")
  private String birth;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  public enum Gender {Male, Female}

  @Column(name = "status", columnDefinition = "varchar(500)")
  @Enumerated(EnumType.STRING)
  private MemberState status;

  @Column(name = "inactiveDate", columnDefinition = "timestamp")
  private LocalDateTime inactiveDate;

  @Column(name = "github", columnDefinition = "varchar(500)")
  private String github;

  @Column(name = "linkedin", columnDefinition = "varchar(500)")
  private String linkedin;

  @Column(name = "discord", columnDefinition = "varchar(500)")
  private String discord;

  @Column(name="introduction", columnDefinition = "varchar(500)")
  private String introduction;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TechStacks> techStackList = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> postList = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TeamMember> teamMemberList = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Authors> authorsList = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MemberProjectMap> memberProjectMapList = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> commentList = new ArrayList<>();

  @OneToMany(mappedBy = "fromMember", fetch = FetchType.LAZY)
  private List<Follow> followings = new ArrayList<>();

  @OneToMany(mappedBy = "toMember", fetch = FetchType.LAZY)
  private List<Follow> followers = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TeamFollow> followedTeams = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Bookmark> bookmarkList = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MemberCategory> memberCategoryList = new ArrayList<>();

  @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
  private MemberImage image;

  @Builder
  public Member(String email, String password, String nickname, String birth, Gender gender, String github, String linkedin, String discord, MemberImage image) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.birth = birth;
    this.gender = gender;
    this.github = github;
    this.linkedin = linkedin;
    this.discord = discord;
    this.image = image;
  }

  public void setImage(MemberImage image) {
    this.image = image;
  }

  public void setMemberProjectMapList(List<MemberProjectMap> projects) {
    this.memberProjectMapList = projects;
  }

  public void setTeamMemberList(List<TeamMember> teamMembers) {
    this.teamMemberList = teamMembers;
  }
}
