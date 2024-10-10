package com.codiary.backend.domain.team.entity;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.team.enumerate.TeamMemberRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMember {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "team_member_id", nullable = false, columnDefinition = "bigint")
  private Long teamMemberId;

  //팀원 직책
  @Column(name = "member_role", columnDefinition = "varchar(500)")
  @Enumerated(EnumType.STRING)
  private TeamMemberRole teamMemberRole;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  @Column(name = "member_position", columnDefinition = "varchar(500)")
  private String memberPosition;

  @Builder
    public TeamMember(TeamMemberRole teamMemberRole, Member member, Team team) {
        this.teamMemberRole = teamMemberRole;
        this.member = member;
        this.team = team;
    }
}
