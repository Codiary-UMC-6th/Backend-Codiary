package com.codiary.backend.global.domain.entity;

import com.codiary.backend.global.domain.common.BaseEntity;
import com.codiary.backend.global.domain.entity.mapping.Authors;
import com.codiary.backend.global.domain.entity.mapping.Categories;
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
public class Post extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id", nullable = false,columnDefinition = "bigint")
  private Long postId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  @Column(name = "post_category",  columnDefinition = "varchar(500)")
  private String postCategory;

  @Column(name = "post_title", nullable = false, columnDefinition = "varchar(500)")
  private String postTitle;

  @Column(name = "post_body", nullable = false, columnDefinition = "varchar(500)")
  private String postBody;

  @Enumerated(EnumType.STRING)
  @Column(name = "post_access", nullable = false, columnDefinition = "varchar(500)")
  private PostAccess postAccess = PostAccess.MEMBER;

  @Column(name = "post_status", nullable = false, columnDefinition = "tinyint")
  private Boolean postStatus;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostPhoto> postPhotoList = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Categories> catecoriesList = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Authors> authorsList = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> commentList = new ArrayList<>();


  public void setMember(Member member) {
    this.member = member;
  }

  public void setTeam(Team team) {
    this.team = team;
  }


}
