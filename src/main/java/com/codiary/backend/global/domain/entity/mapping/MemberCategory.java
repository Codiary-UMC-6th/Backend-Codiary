package com.codiary.backend.global.domain.entity.mapping;

import com.codiary.backend.global.domain.common.BaseEntity;
import com.codiary.backend.global.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    public void setCategories(Categories categories) {
        if (this.categories != null) {
            categories.getMemberCategoryList().remove(this);
        }

        this.categories = categories;

        member.getMemberCategoryList().add(this);
    }



    // 회원별 관심 카테고리탭 수정하기
    public void patchCategories(Categories categories) {
        this.categories = categories;
    }

}
