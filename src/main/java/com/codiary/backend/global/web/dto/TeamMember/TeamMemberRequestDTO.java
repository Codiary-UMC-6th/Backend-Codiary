package com.codiary.backend.global.web.dto.TeamMember;

import com.codiary.backend.global.domain.enums.MemberRole;

public class TeamMemberRequestDTO {

  public record AddMemberDTO(
          Long teamId,
          Long memberId,
          MemberRole memberRole,
          String memberPosition
  ) {}

  public record RemoveMemberDTO(
          Long teamId,
          Long memberId
  ) {}
}
