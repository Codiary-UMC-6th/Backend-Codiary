package com.codiary.backend.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_image_id", nullable = false, columnDefinition = "bigint")
    private Long memberImageId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "image_url", nullable = false, columnDefinition = "varchar(500)")
    private String imageUrl;

    @Builder
    public MemberImage(Member member, String imageUrl) {
        this.member = member;
        this.imageUrl = imageUrl;
    }
}
