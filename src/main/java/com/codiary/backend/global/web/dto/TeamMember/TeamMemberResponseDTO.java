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
  public static class TeamCheckResponseDTO {  //팀 조회
    private Long teamId;
    private String name;
    private String intro;
    private String profilePhoto;
    private String github;
    private String email;
    private String linkedIn;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class TeamMemberDTO {
    private Long teamMemberId;
    private Long teamId;
    private Long memberId;
    private String nickname;
    private MemberRole memberRole;
    private String memberPosition;
  }
}
