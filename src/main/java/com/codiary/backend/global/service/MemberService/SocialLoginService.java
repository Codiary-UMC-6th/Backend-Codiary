package com.codiary.backend.global.service.MemberService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.jwt.JwtTokenProvider;
import com.codiary.backend.global.jwt.TokenInfo;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Value("${kakao.redirect.url}")
    private String kakaoRedirectUrl;

    @Value("${kakao.cliend.id}")
    private String kakaoClientId;


    public String getRedirectUrl() {
        String path = "https://kauth.kakao.com/oauth/authorize?response_type=code";
        String clientId = "&client_id=" + kakaoClientId;
        String redirectUrl = "&redirect_uri=" + kakaoRedirectUrl;

        return path + clientId + redirectUrl;
    }

    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> kakaoLogin(String code) {

        String kakaoAccessToken = getKakaoToken(code);
        String userEmail = getKakaoUserEmail(kakaoAccessToken);

        if (!memberRepository.existsByEmail(userEmail)) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }
        Member member = memberRepository.findByEmail(userEmail).get();


        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getEmail(), member.getMemberId());

        return ApiResponse.of(SuccessStatus.MEMBER_OK, MemberResponseDTO.MemberTokenResponseDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .tokenInfo(tokenInfo)
                .memberId(member.getMemberId())
                .build());
    }

    private String getKakaoToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", kakaoRedirectUrl);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return jsonNode.get("access_token").asText();
    }


    private String getKakaoUserEmail(String kakaoAccessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();

        return email;
    }
}
