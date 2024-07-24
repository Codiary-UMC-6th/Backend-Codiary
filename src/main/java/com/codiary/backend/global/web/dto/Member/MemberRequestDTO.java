package com.codiary.backend.global.web.dto.Member;

import com.codiary.backend.global.domain.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDate;


@Getter
@Setter
public class MemberRequestDTO {


    @Getter
    @Setter
    public static class MemberSignUpRequestDTO {
        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "이메일 형식이 올바르지 않습니다.")
        private String email;
        @Pattern(regexp = "^[a-zA-Z0-9@$!%*#?&]{8,}$", message = "비밀번호 형식이 올바르지 않습니다.")
        private String password;
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        private String nickname;
        private LocalDate birth;
        private Member.Gender gender;
        private String photoUrl;
        private String github;
        private String linkedin;
    }

    @Getter
    @Setter
    public static class MemberLoginRequestDTO {
        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "이메일 형식이 올바르지 않습니다.")
        private String email;
        @Pattern(regexp = "^[a-zA-Z0-9@$!%*#?&]{8,}$", message = "비밀번호 형식이 올바르지 않습니다.")
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }

}
