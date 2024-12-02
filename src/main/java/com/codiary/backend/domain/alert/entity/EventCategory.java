package com.codiary.backend.domain.alert.entity;

public enum EventCategory {
    CONNECT, DISCONNECT,
    BOOKMARK, COMMENT,
    JOIN_TEAM, KICKED_OUT_TEAM,
    TEAM_FOLLOW, FOLLOW,
    FOLLOWING_MEMBER_NEW_POST, FOLLOWING_TEAM_NEW_POST;
}
