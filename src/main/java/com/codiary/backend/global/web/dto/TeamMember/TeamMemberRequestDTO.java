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
  public static class AddTeamMemberDTO {  //팀원 추가
    private Long teamId;
    private Long memberId;
    private MemberRole memberRole;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RemoveTeamMemberDTO { //팀원 삭제
    private Long teamId;
    private Long memberId;
  }
}
