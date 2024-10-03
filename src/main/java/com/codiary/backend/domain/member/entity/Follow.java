package com.codiary.backend.domain.member.entity;

import com.codiary.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id", nullable = false, columnDefinition = "bigint")
    private Long followId;

    @ManyToOne
    @JoinColumn(name = "from_member")
    private Member fromMember;

    @ManyToOne
    @JoinColumn(name = "to_member")
    private Member toMember;

    @Column(name="follow_status", columnDefinition = "tinyint")
    private Boolean followStatus;

    @Builder
    public Follow(Member fromMember, Member toMember, Boolean followStatus) {
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.followStatus = followStatus;
    }

    public void update(Boolean followStatus) {
        this.followStatus = followStatus;
    }
}