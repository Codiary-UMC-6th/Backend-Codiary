package com.codiary.backend.global.web.dto.Team;

import org.springframework.web.multipart.MultipartFile;

public class TeamRequestDTO {

  public record CreateTeamRequestDTO(
          String name,
          String intro,
          String github,
          String email,
          String linkedIn,
          String discord,
          String instagram
  ) {}

  public record UpdateTeamDTO(
          String name,
          String intro,
          String github,
          String linkedIn,
          String discord,
          String instagram
  ) {}

  public record CheckTeam(Long teamId) {}

  public record CreateTeamProjectRequestDTO(String projectName) {}

  public record TeamImageRequestDTO(MultipartFile image) {}
}
