package com.codiary.backend.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Token {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String notAvailableToken;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryTime;
}
