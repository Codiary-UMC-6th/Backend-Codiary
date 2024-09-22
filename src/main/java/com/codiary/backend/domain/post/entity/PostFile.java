package com.codiary.backend.domain.post.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostFile {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_file_id", nullable = false, columnDefinition = "bigint")
  private Long postFileId;

  @JoinColumn(name = "file_name")
  private String fileName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  @Column(name = "file_url", nullable = false, columnDefinition = "varchar(500)")
  private String fileUrl;

}
