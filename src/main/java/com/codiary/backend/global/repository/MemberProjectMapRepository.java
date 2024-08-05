package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.mapping.MemberProjectMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProjectMapRepository extends JpaRepository<MemberProjectMap, Long> {

}
