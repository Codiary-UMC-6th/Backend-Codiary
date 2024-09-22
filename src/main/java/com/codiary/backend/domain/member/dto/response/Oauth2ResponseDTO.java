package com.codiary.backend.domain.member.dto.response;

import lombok.Builder;

@Builder
public record Oauth2ResponseDTO(String redirectUrl) {}
