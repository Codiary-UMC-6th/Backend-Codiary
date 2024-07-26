package com.codiary.backend.global.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPhoto {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_photo_id", nullable = false, columnDefinition = "bigint")
  private Long postPhotoId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  @Column(name = "photo_url", nullable = false, columnDefinition = "varchar(500)")
  private String photoUrl;

}
