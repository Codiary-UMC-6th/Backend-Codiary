package com.codiary.backend.global.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Getter
@Setter
public class Token {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String notAvailableToken;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryTime;
}
