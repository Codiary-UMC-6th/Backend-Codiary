package com.codiary.backend.domain.techstack.entity;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.techstack.enumerate.TechStack;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, columnDefinition = "varchar(50)")
    private TechStack name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public TechStacks(TechStack name, Member member) {
        this.name = name;
        this.member = member;
    }
}
