package com.codiary.backend.global.domain.entity;

import com.codiary.backend.global.domain.common.BaseEntity;
import com.codiary.backend.global.domain.entity.mapping.*;
import com.codiary.backend.global.domain.enums.MemberState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id", nullable = false, columnDefinition = "bigint")
  private Long memberId;

  @Column(name = "email", nullable = false, columnDefinition = "varchar(256)")
  private String email;

  @Column(name = "password", nullable = false, columnDefinition = "varchar(256)")
  private String password;

  @Column(name = "nickname", nullable = false, columnDefinition = "varchar(256)")
  private String nickname;

  @Column(name = "birth", columnDefinition = "varchar(256)")
  private String birth;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  public enum Gender {Male, Female}

  @Column(name = "photoUrl", columnDefinition = "varchar(500)")
  private String photoUrl;

  //계정상태
  @Column(name = "status", columnDefinition = "varchar(500)")
  @Enumerated(EnumType.STRING)
  private MemberState status;

  @Column(name = "inactiveDate", columnDefinition = "timestamp")
  //private Boolean inactiveDate;
  private LocalDateTime inacticeDate;

  @Column(name = "github", columnDefinition = "varchar(500)")
  private String github;

  @Column(name = "linkedin", columnDefinition = "varchar(500)")
  private String linkedin;

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
  private List<Categories> catecoriesList = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> commentList = new ArrayList<>();

  @OneToMany(mappedBy = "fromMember", fetch = FetchType.LAZY)
  private List<Follow> followings = new ArrayList<>();

  @OneToMany(mappedBy = "toMember", fetch = FetchType.LAZY)
  private List<Follow> followers = new ArrayList<>();
}
