package com.codiary.backend.global.domain.entity.mapping;

import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Team;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TeamMember {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long teamMemberId;

  //팀원 직책
  private String teamMemberRole;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

}
