package com.codiary.backend.domain.post.entity;

import com.codiary.backend.domain.category.entity.Category;
import com.codiary.backend.domain.comment.entity.Comment;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.global.common.BaseEntity;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.coauthor.entity.Authors;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

  @Column(name = "post_title", columnDefinition = "varchar(500)")
  private String postTitle;

  @Column(name = "post_body", columnDefinition = "varchar(3000)")
  private String postBody;

  @OneToOne
  @JoinColumn(name = "thumbnail_image_id")
  private PostFile thumbnailImage;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "post_access", nullable = false, columnDefinition = "varchar(500)")
  private PostAccess postAccess = PostAccess.MEMBER;

  @Column(name = "post_status", nullable = false, columnDefinition = "tinyint")
  private Boolean postStatus = true;

  @Builder.Default
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "post_category",
          joinColumns = @JoinColumn(name = "post_id"),
          inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  private List<Category> categoriesList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<PostFile> postFileList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<Authors> authorsList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
  private List<Comment> commentList = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
  private List<Bookmark> bookmarkList = new ArrayList<>();

  public void setMember(Member member) { this.member = member;}
  public void setTeam(Team team) { this.team = team;}
  public void setPostStatus(Boolean postStatus) { this.postStatus = postStatus;}

  public void setProject(Project project) { this.project = project;}

  public void setCategories(List<Categories> categories) {
    this.categoriesList.clear();
    this.categoriesList.addAll(categories);
  }
}
