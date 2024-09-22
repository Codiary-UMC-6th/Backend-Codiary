package com.codiary.backend.domain.member.entity;

import com.codiary.backend.global.common.BaseEntity;
import com.codiary.backend.domain.category.entity.Categories;
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
    private Categories categories;

    public void setMember(Member member) {
        if (this.member != null) {
            member.getMemberCategoryList().remove(this);
        }

        this.member = member;

        member.getMemberCategoryList().add(this);
    }
}
