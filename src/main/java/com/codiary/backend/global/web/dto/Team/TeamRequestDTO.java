package com.codiary.backend.global.web.dto.Team;

import com.codiary.backend.global.domain.enums.PostAccess;
import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class TeamRequestDTO {
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateTeamRequestDTO {  //팀 생성
    private String name;
    private MultipartFile profilePhoto;
    private MultipartFile bannerPhoto;
    private String intro;
    private String github;
    private String email;
    private String linkedIn;
    private String discord;
    private String instagram;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpdateTeamDTO { //팀 프로필 수정
    private String name;
    private String intro;
    private String github;
    private String linkedIn;
    private String discord;
    private String instagram;
  }

  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CheckTeam { //팀 조회
    private Long teamId;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class TeamImageRequestDTO { //이미지 설정
    private MultipartFile image;
  }
}
