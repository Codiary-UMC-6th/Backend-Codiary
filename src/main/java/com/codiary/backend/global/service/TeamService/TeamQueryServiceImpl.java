package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.repository.TeamRepository;
import com.codiary.backend.global.web.dto.Team.TeamResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TeamQueryServiceImpl implements TeamQueryService {

  private final TeamRepository teamRepository;

  @Override
  public TeamResponseDTO.TeamCheckResponseDTO getTeamById(Long teamId) {
    Team team = teamRepository.findById(teamId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));

    return TeamResponseDTO.TeamCheckResponseDTO.builder()
        .teamId(team.getTeamId())
        .name(team.getName())
        .intro(team.getIntro())
        .profilePhoto(team.getProfilePhoto())
        .github(team.getGithub())
        .email(team.getEmail())
        .linkedIn(team.getLinkedin())
        .build();
  }
}
