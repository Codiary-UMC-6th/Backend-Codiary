package com.codiary.backend.global.service.MemberService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.jwt.JwtTokenProvider;
import com.codiary.backend.global.jwt.TokenInfo;
import com.codiary.backend.global.repository.MemberRepository;
import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 카카오
    @Value("${kakao.redirect.url}")
    private String kakaoRedirectUrl;
    @Value("${kakao.cliend.id}")
    private String kakaoClientId;

    // 네이버
    @Value("${naver.redirect.url}")
    private String naverRedirectUrl;
    @Value("${naver.client.id}")
    private String naverClientId;
    @Value("${naver.client.secret}")
    private String naverClientSecret;

    // 깃허브
    @Value("${github.redirect.url}")
    private String githubRedirectUrl;
    @Value("${github.client.id}")
    private String githubClientId;
    @Value("${github.client.secret}")
    private String githubClientSecret;

    // 구글
    @Value("${google.redirect.url}")
    private String googleRedirectUrl;
    @Value("${google.client.id}")
    private String googleClientId;
    @Value("${google.client.secret}")
    private String googleClientSecret;

    // 카카오 로그인
    public String getKakaoRedirectUrl() {
        String path = "https://kauth.kakao.com/oauth/authorize?response_type=code";
        String clientId = "&client_id=" + kakaoClientId;
        String redirectUrl = "&redirect_uri=" + kakaoRedirectUrl;

        return path + clientId + redirectUrl;
    }
    public MemberResponseDTO.MemberTokenResponseDTO kakaoLogin(String code) {

        String kakaoAccessToken = getKakaoToken(code);
        String userEmail = getKakaoUserEmail(kakaoAccessToken);

        if (!memberRepository.existsByEmail(userEmail)) {
            signUp(userEmail);
        }
        Member member = memberRepository.findByEmail(userEmail).get();


        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getEmail(), member.getMemberId());

        return MemberResponseDTO.MemberTokenResponseDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .tokenInfo(tokenInfo)
                .memberId(member.getMemberId())
                .build();
    }

    // 네이버 로그인
    public String getNaverRedirectUrl() {
        String path = "https://nid.naver.com/oauth2.0/authorize";
        String responseType = "?response_type=code";
        String clientId = "&client_id=" + naverClientId;
        String redirectUrl = "&redirect_url=" + naverRedirectUrl;

        return path + responseType + clientId + redirectUrl;
    }
    public MemberResponseDTO.MemberTokenResponseDTO naverLogin(String code, String state) {

        String naverAccessToken = getNaverToken(code, state);
        String userEmail = getNaverUserEmail(naverAccessToken);

        if (!memberRepository.existsByEmail(userEmail)) {
            signUp(userEmail);
        }
        Member member = memberRepository.findByEmail(userEmail).get();

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getEmail(), member.getMemberId());

        return MemberResponseDTO.MemberTokenResponseDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .tokenInfo(tokenInfo)
                .memberId(member.getMemberId())
                .build();
    }

    // 깃허브 로그인
    public String getGithubRedirectUrl() {
        String path = "https://github.com/login/oauth/authorize";
        String clientId = "?client_id=" + githubClientId;
        String redirectUrl = "&redirect_url=" + githubRedirectUrl;

        return path + clientId + redirectUrl;
    }
    public MemberResponseDTO.MemberTokenResponseDTO githubLogin(String code) {

        String githubAccessToken = getGithubToken(code);
        String userEmail = getGithubUserEmail(githubAccessToken);

        if (!memberRepository.existsByEmail(userEmail)) {
            signUp(userEmail);
        }
        Member member = memberRepository.findByEmail(userEmail).get();

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getEmail(), member.getMemberId());

        return MemberResponseDTO.MemberTokenResponseDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .tokenInfo(tokenInfo)
                .memberId(member.getMemberId())
                .build();
    }

    // 구글 로그인
    public String getGoogleRedirectUrl() {
        String path = "https://accounts.google.com/o/oauth2/v2/auth";
        String clientId = "?client_id=" + googleClientId;
        String redirectUri = "&redirect_uri=" + googleRedirectUrl;
        String responseType = "&response_type=code";
        String scope = "&scope=email";

        return path + clientId + redirectUri + responseType + scope;
    }
    public MemberResponseDTO.MemberTokenResponseDTO googleLogin(String code) {

        String googleAccessToken = getGoogleToken(code);
        String userEmail = getGoogleUserEmail(googleAccessToken);

        if (!memberRepository.existsByEmail(userEmail)) {
            signUp(userEmail);
        }
        Member member = memberRepository.findByEmail(userEmail).get();

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getEmail(), member.getMemberId());

        return MemberResponseDTO.MemberTokenResponseDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .tokenInfo(tokenInfo)
                .memberId(member.getMemberId())
                .build();

    }

    private String getGoogleToken(String codeString) {
        String path = "https://oauth2.googleapis.com/token";
        String clientId = "?client_id=" + googleClientId;
        String clientSecret = "&client_secret=" + googleClientSecret;
        String code = "&code=" + codeString;
        String redirectUri = "&redirect_uri=" + googleRedirectUrl;
        String grantType = "&grant_type=authorization_code";

        String url = path + clientId + clientSecret + code + redirectUri + grantType;
        ResponseEntity<JsonNode> response = new RestTemplate().exchange(
                url,
                HttpMethod.POST,
                null,
                JsonNode.class
        );
        return response.getBody().get("access_token").asText();
    }

    private String getGoogleUserEmail(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(headers);
        ResponseEntity<JsonNode> response = new RestTemplate().exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                googleTokenRequest,
                JsonNode.class
        );
        return response.getBody().get("email").asText();
    }



    private String getGithubToken(String codeString) {
        String path = "https://github.com/login/oauth/access_token";
        String clientId = "?client_id=" + githubClientId;
        String clientSecret = "&client_secret=" + githubClientSecret;
        String code = "&code=" + codeString;

        String url = path + clientId + clientSecret + code;
        ResponseEntity<JsonNode> response = new RestTemplate().exchange(
                url,
                HttpMethod.POST,
                null,
                JsonNode.class);
        return response.getBody().get("access_token").asText();
    }

    private String getGithubUserEmail(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/vnd.github+json");
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("X-GitHub-Api-Version", "2022-11-28");

        HttpEntity<MultiValueMap<String, String>> githubTokenRequest = new HttpEntity<>(headers);
        ResponseEntity<JsonNode> response = new RestTemplate().exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                githubTokenRequest,
                JsonNode.class);
        String email = "";
        if (response.getBody().get("email") == null || response.getBody().get("email").asText() == "null") {
            email = response.getBody().get("login").asText() + "@github.com";
        } else {
            email = response.getBody().get("email").asText();
        }
        return email;
    }

    private String getNaverUserEmail(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response = new RestTemplate().exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                naverUserInfoRequest,
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

        String email = jsonNode.get("response").get("email").asText();
        return email;
    }

    private String getNaverToken(String codeString, String stateString) {
        String path = "https://nid.naver.com/oauth2.0/token";
        String grantType = "?grant_type=authorization_code";
        String clientId = "&client_id=" + naverClientId;
        String clientSecret = "&client_secret=" + naverClientSecret;
        String code = "&code=" + codeString;
        String state = "&state=" + ((stateString != null) ? stateString : "test");

        String url = path + grantType + clientId + clientSecret + code + state;
        ResponseEntity<String> response = new RestTemplate().exchange(
                url,
                HttpMethod.POST,
                null,
                String.class);
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

    private Member signUp(String email) {
        Member member = Member.builder()
                .email(email)
                .password("")
                .nickname(email)
                .birth(new LocalDate().toString())
                .gender(Member.Gender.Male)
                .github("")
                .linkedin("")
                .discord("")
                .image(null)
                .build();
        return memberRepository.save(member);
    }

}
