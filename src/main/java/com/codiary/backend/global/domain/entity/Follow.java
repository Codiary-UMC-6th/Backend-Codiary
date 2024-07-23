package com.codiary.backend.global.domain.entity;

import com.codiary.backend.global.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseEntity {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="follow_id", nullable = false, columnDefinition = "bigint")
    private Long FollowId;

    @ManyToOne
    @JoinColumn(name = "from_member")
    private Member fromMember;

    @ManyToOne
    @JoinColumn(name = "to_member")
    private Member toMember;
}
