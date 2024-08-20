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
    private List<TeamMemberResponseDTO.TeamMemberDTO> members; // 팀원 목록 추가
    private Boolean isAdmin;
    private TeamFollowResponseDto followInfo; // 팀 팔로우 정보를 추가
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ProjectsDTO {
    private Long teamId;
    private List<String> projectList;
  }

  @Builder
  public record TeamFollowResponseDto(
      Long followId,
      Long memberId,
      String memberName,
      Long teamId,
      String teamName,
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

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TeamPreviewDTO {
    Long teamId;
    String teamName;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TeamPreviewListDTO {
    List<TeamResponseDTO.TeamPreviewDTO> teams;
  }


}
