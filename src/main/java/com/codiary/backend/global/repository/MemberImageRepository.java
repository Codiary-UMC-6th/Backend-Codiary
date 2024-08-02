package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {
}
