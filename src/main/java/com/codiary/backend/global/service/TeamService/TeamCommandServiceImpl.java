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


}
