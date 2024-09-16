package com.codiary.backend.global.web.dto.Team;

import com.codiary.backend.global.web.dto.TeamMember.TeamMemberResponseDTO;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class TeamResponseDTO {

  @Builder
  public record CreateTeamResponseDTO(
          Long teamId,
          String name,
          String intro,
          String profileImageUrl,
          String bannerImageUrl
  ) {}

  @Builder
  public record UpdateTeamDTO(
          Long teamId,
          String name,
          String intro,
          String profileImageUrl,
          String bannerImageUrl,
          String github,
          String email,
          String linkedIn,
          String discord,
          String instagram
  ) {}

  @Builder
  public record TeamCheckResponseDTO(
          Long teamId,
          String name,
          String intro,
          String profileImageUrl,
          String bannerImageUrl,
          String github,
          String email,
          String linkedIn,
          List<TeamMemberResponseDTO.TeamMemberDTO> members,
          Boolean isAdmin,
          TeamFollowResponseDto followInfo,
          List<ProjectDTO> projects
  ) {
    @Builder
    public record ProjectDTO(
            Long projectId,
            String projectName
    ) {}
  }

  @Builder
  public record ProjectsDTO(
          Long teamId,
          List<String> projectList
  ) {}

  @Builder
  public record TeamFollowResponseDto(
          Long followId,
          Long memberId,
          String memberName,
          Long teamId,
          String teamName,
          Boolean followStatus
  ) {}

  @Builder
  public record TeamImageDTO(
          String url
  ) {}

  @Builder
  public record TeamPreviewDTO(
          Long teamId,
          String teamName
  ) {}

  @Builder
  public record TeamPreviewListDTO(
          List<TeamPreviewDTO> teams
  ) {}
}
