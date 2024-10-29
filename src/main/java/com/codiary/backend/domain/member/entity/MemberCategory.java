package com.codiary.backend.domain.member.entity;

import com.codiary.backend.domain.category.entity.Category;
import com.codiary.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_category_id", nullable = false, columnDefinition = "bigint")
    private Long memberCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public MemberCategory(Member member, Category category) {
        this.member = member;
        this.category = category;
    }
}
