package com.codiary.backend.global.web.dto.TeamMember;

import com.codiary.backend.global.domain.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TeamMemberRequestDTO {
  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class AddMemberDTO {
    private Long teamId;
    private Long memberId;
    private MemberRole memberRole;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RemoveMemberDTO {
    private Long teamId;
    private Long memberId;
  }
}
