package com.codiary.backend.global.repository;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Team;
import com.codiary.backend.global.domain.entity.TeamFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeamFollowRepository extends JpaRepository<TeamFollow, Long> {

  @Query("SELECT tf FROM TeamFollow tf WHERE tf.member = :member AND tf.team = :team")
  Optional<TeamFollow> findByMemberAndTeam(@Param("member") Member member, @Param("team") Team team);

}