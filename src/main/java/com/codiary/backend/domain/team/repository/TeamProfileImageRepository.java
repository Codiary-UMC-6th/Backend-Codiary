package com.codiary.backend.domain.team.repository;

import com.codiary.backend.domain.team.entity.TeamProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamProfileImageRepository extends JpaRepository<TeamProfileImage, Long> {
}
