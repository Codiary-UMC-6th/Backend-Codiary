package com.codiary.backend.domain.alert.entity;

import com.codiary.backend.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "event_category"})})
public class EventReceive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_receive_id", nullable = false, columnDefinition = "bigint")
    private Long eventReceiveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "event_category", nullable = false, columnDefinition = "varchar(256)")
    @Enumerated(EnumType.STRING)
    private EventCategory eventCategory;

    @Column(nullable = false, columnDefinition = "tinyint")
    private Boolean status;

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Builder
    public EventReceive(Member member, EventCategory category, Boolean status) {
        this.member = member;
        this.eventCategory = category;
        this.status = status;
    }
}
