package com.codiary.backend.domain.member.util;

import com.codiary.backend.domain.member.dto.request.MemberRequestDTO;
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

    public static Member updateMember1() {
        return Member.builder()
                .email("u_codiary123@gmail.com")
                .nickname("u_codiary1")
                .password("u_codiary1234")
                .birth("1997-01-01")
                .discord("u_codiary#1234")
                .github("u_codiary#1234")
                .linkedin("u_codiary#1234")
                .build();
    }

    public static MemberRequestDTO.MemberInfoDTO toMemberInfoRequestDTO() {
        return MemberRequestDTO.MemberInfoDTO.builder()
                .email("u_codiary123@gmail.com")
                .nickname("u_codiary1")
                .password("u_codiary1234")
                .birth("1997-01-01")
                .introduction("안녕하세요!")
                .discord("u_codiary#1234")
                .github("u_codiary#1234")
                .linkedin("u_codiary#1234")
                .build();
    }
}
