package com.codiary.backend.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String notAvailableToken;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryTime;

    @Builder
    public Token(String notAvailableToken, Date expiryTime) {
        this.notAvailableToken = notAvailableToken;
        this.expiryTime = expiryTime;
    }
}
