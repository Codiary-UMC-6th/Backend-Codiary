package com.codiary.backend.domain.team.repository;

import com.codiary.backend.domain.team.entity.TeamFollow;

import java.util.List;

public interface TeamFollowRepositoryCustom {


    List<TeamFollow> findFollowersByTeamId(Long teamId);
}
