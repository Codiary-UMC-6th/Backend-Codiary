package com.codiary.backend.global.web.dto.Team;

import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class TeamRequestDTO {
  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateTeamRequestDTO {  // 팀 생성
    private String name;
    private String profilePhoto;
    private String intro;
    private String github;
    private String linkedIn;
    private String discord;
    private String instagram;
    private List<Long> memberIds;
  }
}
