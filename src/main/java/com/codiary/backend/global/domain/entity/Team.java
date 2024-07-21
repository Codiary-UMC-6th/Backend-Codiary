package com.codiary.backend.global.domain.entity;

import com.codiary.backend.global.domain.entity.mapping.TeamMember;
import com.codiary.backend.global.domain.entity.mapping.TeamProjectMap;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
  private List<TeamProjectMap> teamProjectMapList = new ArrayList<>();

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
  private List<Diary> diaryList = new ArrayList<>();

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
  private List<TeamMember> teamMemberList = new ArrayList<>();

}
