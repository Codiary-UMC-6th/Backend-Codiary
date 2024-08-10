package com.codiary.backend.global.repository;

import com.codiary.backend.global.domain.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Boolean existsByNotAvailableToken(String token);
    void deleteByExpiryTimeBefore(Date expiryTime);
}
