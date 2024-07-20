package com.codiary.backend.global.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Team {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "team_id")
  private Long teamId;

  private String name;

  private String intro;

  private String profilePhoto;

  private String github;

  private String email;

  private String linkedin;

  private String instagram;

}
