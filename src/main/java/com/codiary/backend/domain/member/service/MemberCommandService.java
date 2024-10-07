package com.codiary.backend.domain.member.service;

import com.codiary.backend.domain.member.dto.request.MemberRequestDTO;
import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.entity.MemberImage;
import com.codiary.backend.domain.member.entity.Uuid;
import com.codiary.backend.domain.member.repository.MemberImageRepository;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.member.repository.UuidRepository;
import com.codiary.backend.domain.techstack.entity.TechStacks;
import com.codiary.backend.domain.techstack.enumerate.TechStack;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.codiary.backend.global.jwt.JwtTokenProvider;
import com.codiary.backend.global.jwt.SecurityUtil;
import com.codiary.backend.global.jwt.TokenInfo;
import com.codiary.backend.global.s3.AmazonS3Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UuidRepository uuidRepository;
    private final AmazonS3Manager s3Manager;
    private final MemberImageRepository memberImageRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public ApiResponse<String> signUp(MemberRequestDTO.MemberSignUpRequestDTO signUpRequest) {
        signUpRequest.isCorrect();

        if (memberRepository.existsByEmail(signUpRequest.email())
                || memberRepository.existsByNickname(signUpRequest.nickname())) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_EXISTS);
        }

        Member member = Member.builder()
                .email(signUpRequest.email())
                .password(passwordEncoder.encode(signUpRequest.password()))
                .nickname(signUpRequest.nickname())
                .birth(signUpRequest.birth().toString())
                .gender(Member.Gender.Female)
                .github(signUpRequest.github())
                .linkedin(signUpRequest.linkedin())
                .discord(signUpRequest.discord())
                .image(null)
                .build();
        memberRepository.save(member);

        return ApiResponse.of(SuccessStatus.MEMBER_OK, "정상적으로 가입되었습니다.");
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

    @Transactional
    public Member getRequester() {
        String userEmail = SecurityUtil.getCurrentMemberEmail();
        System.out.println(userEmail);

        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return member;
    }

    @Transactional
    public ApiResponse<MemberResponseDTO.MemberImageDTO> updateProfileImage(Member member, MemberRequestDTO.MemberProfileImageRequestDTO request) {
        if (member.getImage() != null) {
            s3Manager.deleteFile(member.getImage().getImageUrl());
            memberImageRepository.delete(member.getImage());
        }

        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
        String fileUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedUuid), request.image());

        MemberImage memberImage = MemberImage.builder()
                .imageUrl(fileUrl)
                .member(member)
                .build();

        MemberImage savedImage = memberImageRepository.save(memberImage);

        MemberResponseDTO.MemberImageDTO response = new MemberResponseDTO.MemberImageDTO(savedImage.getImageUrl());
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, response);
    }

    @Transactional
    public ApiResponse<String> deleteProfileImage(Member member) {
        if (member.getImage() != null) {
            s3Manager.deleteFile(member.getImage().getImageUrl());
            memberImageRepository.delete(member.getImage());
            member.setImage(null);
            memberRepository.save(member);
        }
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, "성공적으로 삭제되었습니다!");
    }

    @Transactional
    public Member updateMemberInfo(Member member, MemberRequestDTO.MemberInfoDTO request){
        member.updateInfo(request);
        return memberRepository.save(member);
    }

    @Transactional
    public Member addTechStack(Long memberId, TechStack techstack) {
        //validatation: member 존재 여부 확인, 기술스택 중복 확인
        Member member = memberRepository.findMemberWithTechStacks(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (member.getTechStackList().stream().anyMatch(stack -> stack.getName().equals(techstack))) {
            throw new GeneralException(ErrorStatus.TECH_STACK_ALREADY_EXISTS);
        }

        //business logic: 기술스택 추가
        List<TechStacks> techStackList = Optional.of(member.getTechStackList()).orElse(new ArrayList<>());
        TechStacks newTechStack = new TechStacks(techstack, member);
        techStackList.add(newTechStack);

        //response: member 반환
        return member;
    }
}
