package com.codiary.backend.global.domain.entity.mapping;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberProjectMap {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberProjectId;

}
