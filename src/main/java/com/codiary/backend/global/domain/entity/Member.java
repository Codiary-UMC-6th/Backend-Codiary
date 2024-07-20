package com.codiary.backend.global.domain.entity;

import com.codiary.backend.global.domain.common.BaseEntity;
import com.codiary.backend.global.domain.enums.MemberRole;
import com.codiary.backend.global.domain.enums.MemberState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long memberId;

  private String email;

  private Long password;

  private String nickname;

  private String birth;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  public enum Gender {Male, Female}

  private String photoUrl;

  private LocalDateTime deletedAt;

  //계정상태
  @Enumerated(EnumType.STRING)
  private MemberState status;

  //휴면 유무
  @Enumerated(EnumType.STRING)
  private MemberRole inactiveDate;

  private String github;

  private String linkedin;

  private String techStacks;

}
