package com.codiary.backend.global.web.dto.Member;

import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.codiary.backend.global.domain.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Getter
@Setter
public class MemberRequestDTO {

    @Getter
    @Setter
    public static class MemberSignUpRequestDTO {
        private String email;
        private String password;
        private String nickname;
        private LocalDate birth;
        private Member.Gender gender;
        private String github;
        private String linkedin;
        private String discord;

        @JsonIgnore
        public Boolean isCorrect() {

            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                throw new MemberHandler(ErrorStatus.MEMBER_WRONG_EMAIL);
            }
            if (!password.matches("^[a-zA-Z0-9@$!%*#?&]{8,}$")) {
                throw new MemberHandler(ErrorStatus.MEMBER_WRONG_PASSWORD);
            }
            if (nickname.trim() == "") {
                throw new MemberHandler(ErrorStatus.MEMBER_NAME_NOT_EXIST);
            }
            return true;
        }
    }

    @Getter
    @Setter
    public static class MemberLoginRequestDTO {
        private String email;
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberProfileImageRequestDTO {
        private MultipartFile image;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberInfoRequestDTO {
        private String birth;
        private String introduction;
        private String github;
        private String linkedin;
        private String discord;
    }

}
