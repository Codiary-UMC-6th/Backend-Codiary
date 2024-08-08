package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.TeamProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamProfileImageRepository extends JpaRepository<TeamProfileImage, Long> {
}
