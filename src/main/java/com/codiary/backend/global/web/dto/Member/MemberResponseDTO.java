package com.codiary.backend.global.web.dto.Member;

import com.codiary.backend.global.jwt.TokenInfo;
import lombok.*;

public class MemberResponseDTO {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberTokenResponseDTO {
        TokenInfo tokenInfo;
        String email;
        String nickname;
    }
}
