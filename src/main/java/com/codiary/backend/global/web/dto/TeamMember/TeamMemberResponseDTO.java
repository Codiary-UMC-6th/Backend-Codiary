package com.codiary.backend.global.web.dto.TeamMember;

import com.codiary.backend.global.domain.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TeamMemberResponseDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TeamMemberViewResponseDTO {
    Long teamMemberId;

  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class TeamMemberDTO { 
    private Long teamMemberId;
    private Long teamId;
    private Long memberId;
    private MemberRole memberRole;
  }
}
