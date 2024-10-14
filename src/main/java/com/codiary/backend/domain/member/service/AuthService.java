package com.codiary.backend.domain.member.service;

import com.codiary.backend.domain.member.converter.MemberConverter;
import com.codiary.backend.domain.member.dto.request.MemberRequestDTO;
import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.codiary.backend.global.jwt.JwtTokenProvider;
import com.codiary.backend.global.jwt.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public String signUp(MemberRequestDTO.MemberSignUpRequestDTO signUpRequest) {
        // validation
        // - 이메일, 닉네임, 비밀번호 적합한지 확인
        // - 존재하는 닉네임/이메일인지 확인
        signUpRequest.isCorrect();

        if (memberRepository.existsByEmail(signUpRequest.email())
                || memberRepository.existsByNickname(signUpRequest.nickname())) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_EXISTS);
        }

        // business logic: dto 바탕으로 member entity 제작 및 저장
        String encodedPassword = passwordEncoder.encode(signUpRequest.password());
        Member member = MemberConverter.toMember(signUpRequest, encodedPassword);
        memberRepository.save(member);

        // response: 정상적으로 계정 생성되었음을 반환
        return "정상적으로 가입되었습니다.";
    }

    @Transactional
    public ApiResponse<String> checkEmailDuplication(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new MemberHandler(ErrorStatus.MEMBER_EMAIL_ALREADY_EXISTS);
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new MemberHandler(ErrorStatus.MEMBER_WRONG_EMAIL);
        }

        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, "사용가능한 이메일입니다!");
    }

    @Transactional
    public ApiResponse<String> checkNicknameDuplication(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new MemberHandler(ErrorStatus.MEMBER_NICKNAME_ALREADY_EXISTS);
        }
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, "사용가능한 닉네임입니다!");
    }


    @Transactional
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> login(MemberRequestDTO.MemberLoginRequestDTO loginRequest) {

        try {
            // 1. Login ID/PW를 기반으로 Authentication 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();

            // 2. 비밀번호 체크
            // loadUserByUsername method 실행됨
            Authentication authentication = authenticationManagerBuilder.getObject()
                    .authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            Member getMember = memberRepository.findByEmail(loginRequest.email()).orElseThrow();
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication, getMember.getMemberId());

            return ApiResponse.of(SuccessStatus.MEMBER_OK, MemberResponseDTO.MemberTokenResponseDTO.builder()
                    .email(getMember.getEmail())
                    .nickname(getMember.getNickname())
                    .tokenInfo(tokenInfo)
                    .memberId(getMember.getMemberId())
                    .build());
        } catch (AuthenticationException e) {
            throw new MemberHandler(ErrorStatus.MEMBER_LOGIN_FAIL);
        }
    }

    @Transactional
    public String logout(String refreshToken) {
        if (redisTemplate.hasKey(refreshToken)) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_LOGGED_OUT);
        }

        if (jwtTokenProvider.validateToken(refreshToken)) {
            Date expirationDate = jwtTokenProvider.getExpirationTimeFromToken(refreshToken);
            long expirationTime = (expirationDate.getTime() - (new Date()).getTime()) / 1000;
            redisTemplate.opsForValue().set(refreshToken, "blacklisted", expirationTime, TimeUnit.SECONDS);
        } else {
            throw new MemberHandler(ErrorStatus.MEMBER_WRONG_TOKEN);
        }
        return "로그아웃되었습니다.";
    }

    @Transactional
    public MemberResponseDTO.TokenRefreshResponseDTO refresh(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new MemberHandler(ErrorStatus.MEMBER_WRONG_TOKEN);
        }

        if (redisTemplate.hasKey(refreshToken)) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_LOGGED_OUT);
        }

        String userEmail = jwtTokenProvider.getUserEmailFromToken(refreshToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(userEmail);
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return MemberResponseDTO.TokenRefreshResponseDTO.builder()
                .accessToken(newAccessToken)
                .email(userEmail)
                .nickname(member.getNickname())
                .memberId(member.getMemberId())
                .build();
    }
}
