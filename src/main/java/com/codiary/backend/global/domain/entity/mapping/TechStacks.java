package com.codiary.backend.global.domain.entity.mapping;

import com.codiary.backend.global.domain.entity.Diary;
import com.codiary.backend.global.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStacks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_stack_id", nullable = false, columnDefinition = "bigint")
    private Long techStackId;

    //우선 String으로 설정, techStack 나오면 enum으로 변경 필요
    @Column(name = "name", nullable = false, columnDefinition = "varchar(50)")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "bigint")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Diary diary;
}
