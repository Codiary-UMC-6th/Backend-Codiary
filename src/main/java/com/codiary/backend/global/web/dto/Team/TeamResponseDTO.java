package com.codiary.backend.global.web.dto.Team;

import com.codiary.backend.global.domain.enums.PostAccess;
import com.codiary.backend.global.jwt.TokenInfo;
import lombok.*;

import java.util.Set;

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

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateTeamDTO { // 팀 프로필 수정
    Long teamId;
    String name;
    String intro;
    String profilePhoto;
    String github;
    String email;
    String linkedIn;
    String discord;
    String instagram;
  }


  @Builder
  public record TeamFollowResponseDto( //팀 팔로우 기능
      Long followId,
      Long followerId,
      String followerName,
      Long followingId,
      String followingName,
      Boolean followStatus
  ) {
  }

}
