package com.codiary.backend.global.web.dto.SocialLogin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Oauth2ResponseDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class kakaoLoginDTO {
        String redirectUrl;
    }
}
