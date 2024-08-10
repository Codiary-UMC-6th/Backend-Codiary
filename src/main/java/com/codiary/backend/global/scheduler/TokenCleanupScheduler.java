package com.codiary.backend.global.scheduler;

import com.codiary.backend.global.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenCleanupScheduler {

    @Autowired
    private TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 1 * * *")
    public void removeExpiredToken() {
        tokenRepository.deleteByExpiryTimeBefore(new Date());
    }
}
