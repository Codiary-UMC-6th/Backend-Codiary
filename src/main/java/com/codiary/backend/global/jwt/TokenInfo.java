package com.codiary.backend.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.Date;

@Builder
@Data
@Getter
public class TokenInfo {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Date refreshTokenExpirationTime;
}
