package com.codiary.backend.domain.alert.entity;

import com.codiary.backend.domain.member.entity.Follow;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.team.entity.TeamFollow;
import com.codiary.backend.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewPostAlert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_post_alert_id", nullable = false, columnDefinition = "bigint")
    private Long newPostAlertId;

    @ManyToOne
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post newPost;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_follow_id")
    private Follow memberFollow;

    @OneToOne
    @JoinColumn(name = "team_follow_id")
    private TeamFollow teamFollow;
}
