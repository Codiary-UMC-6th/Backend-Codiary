package com.codiary.backend.global.domain.entity;

import com.codiary.backend.global.domain.common.BaseEntity;
import com.codiary.backend.global.domain.entity.mapping.Authors;
import com.codiary.backend.global.domain.entity.mapping.Catecories;
import com.codiary.backend.global.domain.entity.mapping.TeamMember;
import com.codiary.backend.global.domain.enums.PostAccess;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Diary extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  private String postCategory;

  private String postTitle;

  private String postBody;

  @Enumerated(EnumType.STRING)
  private PostAccess postAccess;

  private Boolean postStatus;

  @ElementCollection
  private List<String> keywords;

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
  private List<DiaryPhoto> diaryPhotoList = new ArrayList<>();

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
  private List<Catecories> catecoriesList = new ArrayList<>();

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
  private List<Authors> authorsList = new ArrayList<>();

}
