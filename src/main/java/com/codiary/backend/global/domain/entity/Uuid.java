package com.codiary.backend.global.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Uuid {

  @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "uuid", columnDefinition = "varchar(256)")
  private String uuid;
}
