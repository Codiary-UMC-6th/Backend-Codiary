package com.codiary.backend.global.web.dto.SocialLogin;

import lombok.Builder;

@Builder
public record Oauth2ResponseDTO(String redirectUrl) {}
