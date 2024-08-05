package com.codiary.backend.global.web.dto.TeamMember;

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
}
