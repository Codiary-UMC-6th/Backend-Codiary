package com.codiary.backend.domain.member.repository;

import com.codiary.backend.domain.member.entity.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {
}
