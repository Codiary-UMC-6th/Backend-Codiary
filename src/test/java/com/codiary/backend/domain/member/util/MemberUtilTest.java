package com.codiary.backend.domain.member.util;

import com.codiary.backend.domain.member.entity.Member;

public class MemberUtilTest {
    public static Member createMember1() {
        return Member.builder()
                .email("codiary123@gmail.com")
                .nickname("codiary1")
                .password("codiary1234")
                .birth("1996-01-01")
                .discord("codiary#1234")
                .github("codiary#1234")
                .linkedin("codiary#1234")
                .build();
    }

    public static Member createMember2() {
        return Member.builder()
                .email("codiary321@gmail.com")
                .nickname("codiary2")
                .password("codiary1234")
                .birth("1996-01-01")
                .discord("codiary#1234")
                .github("codiary#1234")
                .linkedin("codiary#1234")
                .build();
    }
}
