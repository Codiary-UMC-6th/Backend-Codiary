package com.codiary.backend.domain.member.scheduler;

import com.codiary.backend.domain.member.repository.TokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenCleanupScheduler {

    @Autowired
    private TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void removeExpiredToken() {
        System.out.println("실행");
        tokenRepository.deleteByExpiryTimeBefore(new Date());
    }
}
