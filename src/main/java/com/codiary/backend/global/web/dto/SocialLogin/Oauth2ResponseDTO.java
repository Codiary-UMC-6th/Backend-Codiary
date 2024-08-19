package com.codiary.backend.global.web.dto.SocialLogin;

import lombok.*;

public class Oauth2ResponseDTO {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SocialLoginDTO {
        String redirectUrl;
    }
}
