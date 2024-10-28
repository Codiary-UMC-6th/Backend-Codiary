package com.codiary.backend.domain.team.repository;

import com.codiary.backend.domain.team.entity.TeamBannerImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamBannerImageRepository extends JpaRepository<TeamBannerImage, Long> {
}
