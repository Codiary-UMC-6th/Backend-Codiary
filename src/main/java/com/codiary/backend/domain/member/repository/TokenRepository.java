package com.codiary.backend.domain.member.repository;

import com.codiary.backend.domain.member.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Boolean existsByNotAvailableToken(String token);
    void deleteByExpiryTimeBefore(Date expiryTime);
}
