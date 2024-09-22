package com.codiary.backend.domain.member.repository;

import com.codiary.backend.domain.member.entity.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UuidRepository extends JpaRepository<Uuid, Long> {
}
