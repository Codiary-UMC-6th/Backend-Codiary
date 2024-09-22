package com.codiary.backend.domain.team.entity;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.enumerate.MemberRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeamMember {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "team_member_id", nullable = false, columnDefinition = "bigint")
  private Long teamMemberId;

  //팀원 직책
  @Column(name = "member_role", columnDefinition = "varchar(500)")
  @Enumerated(EnumType.STRING)
  private MemberRole teamMemberRole;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  @Column(name = "member_position", columnDefinition = "varchar(500)")
  private String memberPosition;

}
