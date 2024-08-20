package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Project;
import com.codiary.backend.global.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
  @Query("SELECT t FROM Team t LEFT JOIN FETCH t.followers WHERE t.teamId = :teamId")
  Optional<Team> findByIdWithFollowers(@Param("teamId") Long teamId);

  List<Team> findAllByOrderByTeamIdDesc();
}
