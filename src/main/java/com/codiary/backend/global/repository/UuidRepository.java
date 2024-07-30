package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UuidRepository extends JpaRepository<Uuid, Long> {
}
