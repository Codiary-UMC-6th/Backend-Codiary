package com.codiary.backend.domain.team.entity;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamFollow extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "team_follow_id", nullable = false, columnDefinition = "bigint")
  private Long teamFollowId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id", nullable = false)
  private Team team;

  @Column(name="follow_status", columnDefinition = "tinyint")
  private Boolean followStatus;

  @Builder
  public TeamFollow(Member member, Team team, Boolean followStatus) {
    this.member = member;
    this.team = team;
    this.followStatus = followStatus;
  }

  public void updateFollowStatus(Boolean followStatus) {
    this.followStatus = followStatus;
  }
}