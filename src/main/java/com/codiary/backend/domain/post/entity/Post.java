package com.codiary.backend.domain.post.entity;

import com.codiary.backend.domain.category.entity.Category;
import com.codiary.backend.domain.coauthor.entity.Author;
import com.codiary.backend.domain.comment.entity.Comment;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.dto.request.PostRequestDTO;
import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
  private Boolean postStatus = true;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "post_category",
          joinColumns = @JoinColumn(name = "post_id"),
          inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  private List<Category> categoriesList = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<PostFile> postFileList = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<Author> authorList = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
  private List<Comment> commentList = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
  private List<Bookmark> bookmarkList = new ArrayList<>();

  public void setMember(Member member) { this.member = member;}
  public void setTeam(Team team) { this.team = team;}
  public void setPostStatus(Boolean postStatus) { this.postStatus = postStatus;}

  public void setProject(Project project) { this.project = project;}


  public void update(PostRequestDTO.UpdatePostDTO request) {
    this.postTitle = request.getPostTitle();
    this.postBody = request.getPostBody();
    this.postAccess = request.getPostAccess();
    this.postStatus = request.getPostStatus();
  }

  public void setCategories(List<Category> categories) {
    this.categoriesList.clear();
    this.categoriesList.addAll(categories);
  }

  @Builder
  public Post(String postTitle, String postBody, Member member, Team team, Project project, Boolean postStatus, PostAccess postAccess) {
    this.postTitle = postTitle;
    this.postBody = postBody;
    this.member = member;
    this.team = team;
    this.project = project;
    this.postStatus = postStatus;
    this.postAccess = postAccess;
    this.thumbnailImage = null;
    this.postFileList = new ArrayList<>();
    this.authorList = new ArrayList<>();
    this.categoriesList = new ArrayList<>();
    this.commentList = new ArrayList<>();
    this.bookmarkList = new ArrayList<>();
  }

  @Builder
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class PostAdjacent{
    Post laterPost;
    Post olderPost;
  }

  public void setThumbnailImage(PostFile postFile) {
    this.thumbnailImage = postFile;
  }

  public void setAuthorList(List<Author> authorList) {
    this.authorList = authorList;
  }
}
