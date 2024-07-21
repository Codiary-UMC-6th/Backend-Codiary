package com.codiary.backend.global.domain.entity;

import com.codiary.backend.global.domain.common.BaseEntity;
import com.codiary.backend.global.domain.enums.PostAccess;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Diary extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long diaryId;

  // String?
  private String diaryCategory;

  private String diaryTitle;

  private String diaryBody;

  private PostAccess diaryAccess;

  @ElementCollection
  private List<String> keywords;
  // 게시글 임시저장?

}
