package com.codiary.backend.domain.post.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostFile {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_file_id", nullable = false, columnDefinition = "bigint")
  private Long postFileId;

  @JoinColumn(name = "file_name")
  private String fileName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  @Column(nullable = false, columnDefinition = "varchar(500)")
  private String fileUrl;

}
