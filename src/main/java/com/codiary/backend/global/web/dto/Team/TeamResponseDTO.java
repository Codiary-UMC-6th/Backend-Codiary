package com.codiary.backend.global.web.dto.Team;

import com.codiary.backend.global.jwt.TokenInfo;
import lombok.*;

public class TeamResponseDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CreateTeamResponseDTO {  // 팀 생성
    Long teamId;
    String name;
    String intro;
    String profilePhoto;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TeamCheckResponseDTO {  // 팀 조회
    Long teamId;
    String name;
    String intro;
    String profilePhoto;
    String github;
    String email;
    String linkedIn;
  }

}
