package com.codiary.backend.domain.member.service;

import com.codiary.backend.domain.member.converter.MemberConverter;
import com.codiary.backend.domain.member.dto.request.MemberRequestDTO;
import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
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
    public String checkEmailDuplication(String email) {
        // validation: 이메일 형식 확인
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new MemberHandler(ErrorStatus.MEMBER_WRONG_EMAIL);
        }

        // business logic: 이메일 중복 확인
        if (memberRepository.existsByEmail(email)) {
            throw new MemberHandler(ErrorStatus.MEMBER_EMAIL_ALREADY_EXISTS);
        }

        // response: 사용가능 여부 반환
        return "사용가능한 이메일입니다!";
    }

    @Transactional
    public String checkNicknameDuplication(String nickname) {
        // validation: 닉네임 형식 확인
        if (nickname.trim().isEmpty()) {
            throw new MemberHandler(ErrorStatus.MEMBER_NAME_NOT_EXIST);
        }

        // business logic: 닉네임 중복 확인
        if (memberRepository.existsByNickname(nickname)) {
            throw new MemberHandler(ErrorStatus.MEMBER_NICKNAME_ALREADY_EXISTS);
        }

        return "사용가능한 닉네임입니다!";
    }


    @Transactional
    public MemberResponseDTO.MemberTokenResponseDTO login(MemberRequestDTO.MemberLoginRequestDTO loginRequest) {

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

            return MemberResponseDTO.MemberTokenResponseDTO.builder()
                    .email(getMember.getEmail())
                    .nickname(getMember.getNickname())
                    .tokenInfo(tokenInfo)
                    .memberId(getMember.getMemberId())
                    .build();
        } catch (AuthenticationException e) {
            throw new MemberHandler(ErrorStatus.MEMBER_LOGIN_FAIL);
        }
    }

    @Transactional
    public String logout(String refreshToken) {
        // validation: 로그인 상태인지 확인, 정상 토큰인지 확인
        if (redisTemplate.hasKey(refreshToken)) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_LOGGED_OUT);
        }
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new MemberHandler(ErrorStatus.MEMBER_WRONG_TOKEN);
        }

        // business logic: 만료 날짜 설정 및 refresh token blacklist에 추가
        Date expirationDate = jwtTokenProvider.getExpirationTimeFromToken(refreshToken);
        long expirationTime = (expirationDate.getTime() - (new Date()).getTime()) / 1000;
        redisTemplate.opsForValue().set(refreshToken, "blacklisted", expirationTime, TimeUnit.SECONDS);

        return "로그아웃되었습니다.";
    }

    @Transactional
    public MemberResponseDTO.TokenRefreshResponseDTO refresh(String refreshToken) {
        // validation: 정상 토큰 확인 및 로그아웃한 유저인지 확인
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new MemberHandler(ErrorStatus.MEMBER_WRONG_TOKEN);
        }
        if (redisTemplate.hasKey(refreshToken)) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_LOGGED_OUT);
        }

        // business logic: access token 재할당
        String userEmail = jwtTokenProvider.getUserEmailFromToken(refreshToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(userEmail);
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // response: 만들어진 토큰 반환
        return MemberResponseDTO.TokenRefreshResponseDTO.builder()
                .accessToken(newAccessToken)
                .email(userEmail)
                .nickname(member.getNickname())
                .memberId(member.getMemberId())
                .build();
    }
}
