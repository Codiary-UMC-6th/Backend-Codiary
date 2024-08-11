package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.domain.entity.*;
import com.codiary.backend.global.jwt.SecurityUtil;
import com.codiary.backend.global.repository.*;
import com.codiary.backend.global.s3.AmazonS3Manager;
import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;
import com.codiary.backend.global.web.dto.Team.TeamRequestDTO;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TeamCommandServiceImpl implements TeamCommandService {

  private final TeamRepository teamRepository;
  private final TeamFollowService teamFollowService;
  private final MemberRepository memberRepository;
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

    Team savedTeam = teamRepository.save(team);
    return savedTeam;
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
  @Transactional
  public TeamFollow followTeam(Long teamId, Member fromMember) {
    return teamFollowService.followTeam(teamId, fromMember);
  }

  @Override
  @Transactional(readOnly = true)
  public Member getRequester() {
    String userEmail = SecurityUtil.getCurrentMemberEmail();
    System.out.println(userEmail);

    Member member = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

    // 컬렉션 초기화: size()를 호출하여 지연 로딩된 컬렉션 초기화
    member.getFollowedTeams().size();

    return member;
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