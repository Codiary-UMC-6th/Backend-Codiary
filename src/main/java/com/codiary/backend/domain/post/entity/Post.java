package com.codiary.backend.domain.post.entity;

import com.codiary.backend.domain.comment.entity.Comment;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.global.common.BaseEntity;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.coauthor.entity.Authors;
import com.codiary.backend.domain.category.entity.Categories;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id", nullable = false,columnDefinition = "bigint")
  private Long postId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  private Project project;

  @Column(name = "post_title", nullable = false, columnDefinition = "varchar(500)")
  private String postTitle;

  @Column(name = "post_body", nullable = false, columnDefinition = "varchar(3000)")
  private String postBody;

  @OneToOne
  @JoinColumn(name = "thumbnail_image_id")
  private PostFile thumbnailImage;

  @Enumerated(EnumType.STRING)
  @Column(name = "post_access", nullable = false, columnDefinition = "varchar(500)")
  private PostAccess postAccess = PostAccess.MEMBER;

  @Column(name = "post_status", nullable = false, columnDefinition = "tinyint")
  private Boolean postStatus;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "post_category",
          joinColumns = @JoinColumn(name = "post_id"),
          inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  private List<Categories> categoriesList = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<PostFile> postFileList = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<Authors> authorsList = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
  private List<Comment> commentList = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
  private List<Bookmark> bookmarkList = new ArrayList<>();

  public void setMember(Member member) { this.member = member;}
  public void setTeam(Team team) { this.team = team;}

}
