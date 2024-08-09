package com.codiary.backend.global.domain.entity;

import com.codiary.backend.global.domain.common.BaseEntity;
import com.codiary.backend.global.domain.entity.mapping.Authors;
import com.codiary.backend.global.domain.entity.mapping.Categories;
import com.codiary.backend.global.domain.enums.PostAccess;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

  @ManyToOne(fetch = FetchType.LAZY)
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

  @Column(name = "post_body", nullable = false, columnDefinition = "varchar(500)")
  private String postBody;

  @OneToOne
  @JoinColumn(name = "thumbnail_image_id")
  private PostFile thumbnailImage;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "post_access", nullable = false, columnDefinition = "varchar(500)")
  private PostAccess postAccess = PostAccess.MEMBER;

  @Column(name = "post_status", nullable = false, columnDefinition = "tinyint")
  private Boolean postStatus;

  @Builder.Default
  @ManyToMany
  @JoinTable(
          name = "post_category",
          joinColumns = @JoinColumn(name = "post_id"),
          inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  private List<Categories> categoriesList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostFile> postFileList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Authors> authorsList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> commentList = new ArrayList<>();

 
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Bookmark> bookmarkList = new ArrayList<>();

  public void setProject(Project project) { this.project = project;}

  public void update(PostRequestDTO.UpdatePostDTO request) {
    this.postTitle = request.getPostTitle();
    this.postBody = request.getPostBody();
    this.postAccess = request.getPostAccess();
//    List<Categories> categoryList = request.getPostCategory().stream()
//            .map(categoryName -> Categories.createCategory(this, categoryName, this.member))
//            .collect(Collectors.toList());
//    setCategories(categoryList);
    this.postStatus = request.getPostStatus();
  }

  public void setMember(Member member) { this.member = member;}
  public void setTeam(Team team) { this.team = team;}
  public void setPostStatus(Boolean postStatus) { this.postStatus = postStatus;}

  public void setCategories(List<Categories> categories) {
    this.categoriesList.clear();
    this.categoriesList.addAll(categories);
  }

  @Builder
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class PostAdjacent{
    Post laterPost;
    Post olderPost;
  }

}
