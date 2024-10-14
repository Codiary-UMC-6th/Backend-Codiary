package com.codiary.backend.domain.member.dto.request;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class MemberRequestDTO {

    // 회원가입 요청 DTO
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record MemberSignUpRequestDTO(
            String email,
            String password,
            String nickname,
            LocalDate birth,
            Member.Gender gender,
            String github,
            String linkedin,
            String discord) {

        @JsonIgnore
        public Boolean isCorrect() {
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                throw new MemberHandler(ErrorStatus.MEMBER_WRONG_EMAIL);
            }
            if (!password.matches("^[a-zA-Z0-9@$!%*#?&]{8,}$")) {
                throw new MemberHandler(ErrorStatus.MEMBER_WRONG_PASSWORD);
            }
            if (nickname.trim().isEmpty()) {
                throw new MemberHandler(ErrorStatus.MEMBER_NAME_NOT_EXIST);
            }
            return true;
        }
    }

    // 로그인 요청 DTO
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record MemberLoginRequestDTO(String email, String password) {
        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }

    // 프로필 이미지 요청 DTO
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record MemberProfileImageRequestDTO(MultipartFile image) {
    }

    // 회원 정보 요청 DTO
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record MemberInfoDTO(
            String birth,
            String introduction,
            String github,
            String linkedin,
            String discord) {
    }

    // refresh token 요청 DTO
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record refreshRequestDTO(
            String refreshToken) {
    }

    // 이메일 확인 요청 DTO
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record emailDuplicationCheckRequestDTO(
            String email) {
    }

    // 이메일 확인 요청 DTO
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record nickNameDuplicationCheckRequestDTO(
            String nickName) {
    }
}
