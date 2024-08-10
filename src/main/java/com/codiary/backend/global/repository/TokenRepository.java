package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TokenRepository extends JpaRepository<Token, Long> {
    void deleteByExpiryTimeBefore(Date expiryTime);
}
