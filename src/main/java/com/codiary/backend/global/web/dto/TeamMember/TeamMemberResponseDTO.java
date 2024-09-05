package com.codiary.backend.global.web.dto.TeamMember;

import com.codiary.backend.global.domain.enums.MemberRole;
import lombok.Builder;

public class TeamMemberResponseDTO {

  @Builder
  public record TeamMemberDTO(
          Long teamMemberId,
          Long teamId,
          Long memberId,
          String nickname,
          MemberRole memberRole,
          String memberPosition
  ) {}
}
