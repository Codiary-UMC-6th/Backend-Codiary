package com.codiary.backend.global.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DiaryPhoto {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postPhotoId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Diary diary;

  private String photoUrl;

}
