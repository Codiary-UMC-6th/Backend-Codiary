package com.codiary.backend.domain.team.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_profile_image_id", nullable = false, columnDefinition = "bigint")
    private Long teamProfileImageId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "image_url", nullable = false, columnDefinition = "varchar(500)")
    private String imageUrl;

}
