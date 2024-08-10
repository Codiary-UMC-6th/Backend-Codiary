package com.codiary.backend.global.service.TeamMemberService;

import com.codiary.backend.global.domain.entity.mapping.TeamMember;
import com.codiary.backend.global.web.dto.TeamMember.TeamMemberRequestDTO;

public interface TeamMemberCommandService {

  // 팀원 추가
  TeamMember addMember(TeamMemberRequestDTO.AddMemberDTO request, Long adminId);

  // 팀원 삭제
  void removeMember(TeamMemberRequestDTO.RemoveMemberDTO request, Long adminId);
}
