package com.codiary.backend.global.web.dto.Team;

import com.codiary.backend.global.domain.enums.PostAccess;
import com.codiary.backend.global.jwt.TokenInfo;
import com.codiary.backend.global.web.dto.TeamMember.TeamMemberResponseDTO;
import lombok.*;

import java.util.List;
import java.util.Set;

public class TeamResponseDTO {

  @Builder
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CreateTeamResponseDTO {  // 팀 생성
    Long teamId;
    String name;
    String intro;
    String profileImageUrl;
    String bannerImageUrl;
  }

  @Getter
  @Builder
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateTeamDTO { // 팀 프로필 수정
    Long teamId;
    String name;
    String intro;
    String profileImageUrl;
    String bannerImageUrl;
    String github;
    String email;
    String linkedIn;
    String discord;
    String instagram;
  }

  @Builder
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TeamCheckResponseDTO {  // 팀 조회
    Long teamId;
    String name;
    String intro;
    String profileImageUrl;
    String bannerImageUrl;
    String github;
    String email;
    String linkedIn;
    List<TeamMemberResponseDTO.TeamMemberDTO> members; // 팀원 목록 추가
    boolean isAdmin;  //관리자 여부
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ProjectDTO {  //프로젝트 생성
    private Long projectId;
    private String projectName;
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

  @Builder
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TeamImageDTO {
    String url;
  }

}
