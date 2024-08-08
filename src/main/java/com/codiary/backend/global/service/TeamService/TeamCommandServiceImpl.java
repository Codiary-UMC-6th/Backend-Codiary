package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.domain.entity.*;
import com.codiary.backend.global.repository.TeamBannerImageRepository;
import com.codiary.backend.global.repository.TeamProfileImageRepository;
import com.codiary.backend.global.repository.TeamRepository;
import com.codiary.backend.global.repository.UuidRepository;
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
        .build();

    Team savedTeam = teamRepository.save(team);

    // 배너 이미지 저장
    String bannerUuid = UUID.randomUUID().toString();
    Uuid savedBannerUuid = uuidRepository.save(Uuid.builder().uuid(bannerUuid).build());
    String bannerImageUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedBannerUuid), request.getBannerPhoto());
    TeamBannerImage bannerImage = TeamBannerImage.builder()
            .imageUrl(bannerImageUrl)
            .team(savedTeam)
            .build();
    savedTeam.setBannerImage(bannerImageRepository.save(bannerImage));

    // 프로필 이미지 저장
    String profileUuid = UUID.randomUUID().toString();
    Uuid savedProfileUuid = uuidRepository.save(Uuid.builder().uuid(profileUuid).build());
    String profileImageUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedProfileUuid), request.getProfilePhoto());
    TeamProfileImage profileImage = TeamProfileImage.builder()
            .imageUrl(profileImageUrl)
            .team(savedTeam)
            .build();
    savedTeam.setProfileImage(profileImageRepository.save(profileImage));

    return teamRepository.save(savedTeam);
  }

  @Override
  @Transactional
  public Team updateTeam(Long teamId, TeamRequestDTO.UpdateTeamDTO request) {
    Team team = teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));

    team.setName(request.getName());
    team.setIntro(request.getIntro());
    team.setGithub(request.getGithub());
    team.setLinkedin(request.getLinkedIn());
    team.setInstagram(request.getInstagram());

    return teamRepository.save(team);
  }

  @Override
  public ApiResponse<TeamResponseDTO.TeamImageDTO> updateTeamBannerImage(Long teamId, TeamRequestDTO.TeamImageRequestDTO request) {
    Team team = teamRepository.findById(teamId).orElseThrow(); // 예외 처리 필요

    s3Manager.deleteFile(team.getBannerImage().getImageUrl());
    teamBannerImageRepository.delete(team.getBannerImage());

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

    s3Manager.deleteFile(team.getProfileImage().getImageUrl());
    teamProfileImageRepository.delete(team.getProfileImage());

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


}
