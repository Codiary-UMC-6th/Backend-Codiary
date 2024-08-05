package com.codiary.backend.global.web.dto.TeamMember;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TeamMemberRequestDTO {
  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class AddTeamMemberRequestDTO {  //팀원 추가
    private String name;
    private String profilePhoto;
    private String intro;
    private String github;
    private String linkedIn;
    private String discord;
    private String instagram;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DeleteMemberRequestDTO {  //팀원 삭제
    private String name;
    private String profilePhoto;
    private String intro;
    private String github;
    private String linkedIn;
    private String discord;
    private String instagram;
  }
}
