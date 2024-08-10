package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.TeamConverter;
import com.codiary.backend.global.domain.entity.*;
import com.codiary.backend.global.domain.entity.mapping.TeamMember;
import com.codiary.backend.global.domain.entity.mapping.TeamProjectMap;
import com.codiary.backend.global.domain.enums.MemberRole;
import com.codiary.backend.global.repository.*;
import com.codiary.backend.global.s3.AmazonS3Manager;
import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;
import com.codiary.backend.global.web.dto.Team.TeamRequestDTO;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TeamCommandServiceImpl implements TeamCommandService {

  private final TeamRepository teamRepository;
  private final MemberRepository memberRepository;
  private final TeamMemberRepository teamMemberRepository;
  private final ProjectRepository projectRepository;
  private final TeamProjectMapRepository teamProjectMapRepository;
  private final UuidRepository uuidRepository;
  private final AmazonS3Manager s3Manager;
  private final TeamBannerImageRepository bannerImageRepository;
  private final TeamProfileImageRepository profileImageRepository;
  private final TeamProfileImageRepository teamProfileImageRepository;
  private final TeamBannerImageRepository teamBannerImageRepository;

  @Override
  @Transactional
  public Team createTeam(TeamRequestDTO.CreateTeamRequestDTO request) {
    Team team = Team.builder()
        .name(request.getName())
        .intro(request.getIntro())
        .github(request.getGithub())
        .email(request.getEmail())
        .linkedin(request.getLinkedIn())
        .instagram(request.getInstagram())
        .bannerImage(null)
        .profileImage(null)
        .build();

    teamRepository.save(team);

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

    // 팀 생성자에게 관리자 역할 부여
    TeamMember teamMember = TeamMember.builder()
        .team(team)
        .member(member)
        .teamMemberRole(MemberRole.ADMIN)
        .build();

    teamMemberRepository.save(teamMember);

    return TeamConverter.toCreateTeamResponseDTO(team);
  }

  @Override
  @Transactional
  public Team updateTeam(Long teamId, Long memberId, TeamRequestDTO.UpdateTeamDTO request) {
    Team team = teamRepository.findById(teamId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));

    // 관리자 여부 확인
    boolean isAdmin = team.getTeamMemberList().stream()
        .anyMatch(member -> member.getMember().getMemberId().equals(memberId) &&
            member.getTeamMemberRole() == MemberRole.ADMIN);

    if (!isAdmin) {
      throw new IllegalStateException("권한이 없습니다.");
    }

    team.setName(request.getName());
    team.setIntro(request.getIntro());
    team.setGithub(request.getGithub());
    team.setLinkedin(request.getLinkedIn());
    team.setDiscord(request.getDiscord());
    team.setInstagram(request.getInstagram());

    return teamRepository.save(team);
  }


  @Override
  @Transactional
  public TeamResponseDTO.ProjectDTO createProject(Long teamId, Long memberId, TeamRequestDTO.CreateProjectDTO request) {
    Team team = teamRepository.findById(teamId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));

    // 관리자 여부 확인
    boolean isAdmin = team.getTeamMemberList().stream()
        .anyMatch(member -> member.getMember().getMemberId().equals(memberId) &&
            member.getTeamMemberRole() == MemberRole.ADMIN);

    if (!isAdmin) {
      throw new IllegalStateException("권한이 없습니다.");
    }

    Project project = Project.builder()
        .projectName(request.getProjectName())
        .build();

    projectRepository.save(project);

    TeamProjectMap teamProjectMap = TeamProjectMap.builder()
        .team(team)
        .project(project)
        .build();

    teamProjectMapRepository.save(teamProjectMap);

    return TeamConverter.toProjectDTO(project);
  }

  @Override
  public ApiResponse<TeamResponseDTO.TeamImageDTO> updateTeamBannerImage(Long teamId, TeamRequestDTO.TeamImageRequestDTO request) {
    Team team = teamRepository.findById(teamId).orElseThrow(); // 예외 처리 필요

    if (team.getBannerImage() != null) {
      s3Manager.deleteFile(team.getBannerImage().getImageUrl());
      teamBannerImageRepository.delete(team.getBannerImage());
    }

    String uuid = UUID.randomUUID().toString();
    Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
    String fileUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedUuid), request.getImage());

    TeamBannerImage bannerImage = TeamBannerImage.builder()
            .imageUrl(fileUrl)
            .team(team)
            .build();

    TeamBannerImage savedImage = bannerImageRepository.save(bannerImage);
    TeamResponseDTO.TeamImageDTO response = new TeamResponseDTO.TeamImageDTO(savedImage.getImageUrl());
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, response);
  }

  @Override
  public ApiResponse<TeamResponseDTO.TeamImageDTO> updateTeamProfileImage(Long teamId, TeamRequestDTO.TeamImageRequestDTO request) {
    Team team = teamRepository.findById(teamId).orElseThrow(); // 예외 처리 필요

    if (team.getProfileImage() != null) {
      s3Manager.deleteFile(team.getProfileImage().getImageUrl());
      teamProfileImageRepository.delete(team.getProfileImage());
    }

    String uuid = UUID.randomUUID().toString();
    Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
    String fileUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedUuid), request.getImage());

    TeamProfileImage profileImage = TeamProfileImage.builder()
            .imageUrl(fileUrl)
            .team(team)
            .build();

    TeamProfileImage savedImage = profileImageRepository.save(profileImage);
    TeamResponseDTO.TeamImageDTO response = new TeamResponseDTO.TeamImageDTO(savedImage.getImageUrl());
    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, response);
  }

  @Override
  public ApiResponse<String> deleteTeamBannerImage(Long teamId) {
    Team team = teamRepository.findById(teamId).orElseThrow(); // 예외 처리 필요

    if (team.getBannerImage() != null) {
      s3Manager.deleteFile(team.getBannerImage().getImageUrl());
      teamBannerImageRepository.delete(team.getBannerImage());
      team.setBannerImage(null);
      teamRepository.save(team);
    }

    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, "성공적으로 삭제되었습니다!");
  }

  @Override
  public ApiResponse<String> deleteTeamProfileImage(Long teamId) {
    Team team = teamRepository.findById(teamId).orElseThrow(); // 예외 처리 필요

    if (team.getProfileImage() != null) {
      s3Manager.deleteFile(team.getProfileImage().getImageUrl());
      teamProfileImageRepository.delete(team.getProfileImage());
      team.setProfileImage(null);
      teamRepository.save(team);
    }

    return ApiResponse.onSuccess(SuccessStatus.TEAM_OK, "성공적으로 삭제되었습니다!");
  }

}
