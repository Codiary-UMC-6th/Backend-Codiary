package com.codiary.backend.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Uuid {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "uuid", columnDefinition = "varchar(256)")
  private String uuid;

  @Builder
  public Uuid(String uuid) {
    this.uuid = uuid;
  }
}
