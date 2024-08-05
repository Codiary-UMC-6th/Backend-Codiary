package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.repository.TeamRepository;
import com.codiary.backend.global.web.dto.Team.TeamRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TeamCommandServiceImpl implements TeamCommandService {

  private final TeamRepository teamRepository;
  @Override
  @Transactional
  public Team createTeam(TeamRequestDTO.CreateTeamRequestDTO request) {
    Team team = Team.builder()
        .name(request.getName())
        .intro(request.getIntro())
        .profilePhoto(request.getProfilePhoto())
        .github(request.getGithub())
        .email(request.getEmail())
        .linkedin(request.getLinkedIn())
        .instagram(request.getInstagram())
        .build();

    return teamRepository.save(team);
  }
}
