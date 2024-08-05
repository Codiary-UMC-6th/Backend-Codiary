package com.codiary.backend.global.service.TeamService;

import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.web.dto.Team.TeamRequestDTO;
import com.codiary.backend.global.web.dto.TeamMember.TeamMemberRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface TeamCommandService {

  //팀 생성
  Team createTeam(TeamRequestDTO.CreateTeamRequestDTO request);

  //팀 프로필 수정
  Team updateTeam(Long teamId);


}
