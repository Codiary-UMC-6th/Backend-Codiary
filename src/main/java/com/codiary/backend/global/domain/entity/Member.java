package com.codiary.backend.global.domain.entity;

import com.codiary.backend.global.domain.common.BaseEntity;
import com.codiary.backend.global.domain.entity.mapping.*;
import com.codiary.backend.global.domain.enums.MemberRole;
import com.codiary.backend.global.domain.enums.MemberState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long memberId;

  private String email;

  private String password;

  private String nickname;

  private String birth;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  public enum Gender {Male, Female}

  private String photoUrl;

  //계정상태
  @Enumerated(EnumType.STRING)
  private MemberState status;

  private Boolean inactiveDate;

  private String github;

  private String linkedin;

  private String techStacks;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Diary> diaryList = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<TeamMember> teamMemberList = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Authors> authorsList = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<MemberProjectMap> memberProjectMapList = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Catecories> catecoriesList = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Comment> commentList = new ArrayList<>();

}
