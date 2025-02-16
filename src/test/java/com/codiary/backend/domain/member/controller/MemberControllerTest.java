package com.codiary.backend.domain.member.controller;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.util.MemberUtilTest;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.util.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@AutoConfigureMockMvc
@DisplayName("MemberController 테스트")
public class MemberControllerTest extends ControllerTest {
    private Member member2;

    @BeforeEach
    void setUp() {
        member2 = MemberUtilTest.createMember2();
        member2.setMemberId(2L);
    }

    @Nested
    @DisplayName("사용자 기본 프로필 조회 메서드 테스트")
    class GetUserProfileTest {

        @Test
        @DisplayName("✅ member_id가 주어지면 해당하는 사용자 정보를 조회한다.")
        void getUserProfile_ShouldReturnUserProfile() throws Exception {
            // given: 조회하는 사용자 정보와 accessToken, 조회 대상 사용자의 정보 Mocking
            String accessToken = jwtTokenProvider.generateToken(member1.getEmail()).getAccessToken();
            given(memberRepository.findMemberWithTechStacksAndProjectsAndTeam(member1.getMemberId()))
                    .willReturn(Optional.of(member1));
            given(memberRepository.findMemberWithTechStacksAndProjectsAndTeam(member2.getMemberId()))
                    .willReturn(Optional.of(member2));

            // when & then: 사용자 정보 조회 API 호출 시 조회 대상 사용자의 정보 반환
            mockMvc.perform(get("/api/v2/member/profile/{member_id}", member2.getMemberId())
                            .header("Authorization", "Bearer " + accessToken))
                    .andDo(print())
                    .andExpect(status().isOk()) // HTTP status 200 OK 응답 검증
                    .andExpect(jsonPath("$.isSuccess").value(true)) // isSuccess 값 검증
                    .andExpect(jsonPath("$.code").value("MEMBER_1000")) // code 값 검증
                    .andExpect(jsonPath("$.message").value("성공입니다.")) // message 값 검증
                    .andExpect(jsonPath("$.result.current_member_id").value(member1.getMemberId())) // current_member_id 값 검증
                    .andExpect(jsonPath("$.result.user_id").value(member2.getMemberId())) // user_id 값 검증
                    .andExpect(jsonPath("$.result.user_name").value(member2.getNickname())); // user_name 값 검증
        }

        @Test
        @DisplayName("❌ 존재하지 않는 사용자 프로필 조회 시 예외가 발생한다.")
        void getUserProfile_ShouldReturnError_WhenUserNotFound() throws Exception {
            // given: 존재하지 않는 사용자 ID를 조회하는 경우
            Long nonExistentMemberId = 999L;
            String accessToken = jwtTokenProvider.generateToken(member1.getEmail()).getAccessToken();
            given(memberRepository.findMemberWithTechStacksAndProjectsAndTeam(nonExistentMemberId))
                    .willReturn(Optional.empty());

            // when & then: 존재하지 않는 사용자 ID로 사용자 정보 조회 시 오류 발생
            mockMvc.perform(get("/api/v2/member/profile/{member_id}", nonExistentMemberId)
                            .header("Authorization", "Bearer " + accessToken))
                    .andDo(print())
                    .andExpect(status().isBadRequest()) // HTTP status 400 Bad Request
                    .andExpect(jsonPath("$.isSuccess").value(false)) // 실패 여부 확인
                    .andExpect(jsonPath("$.code").value(ErrorStatus.MEMBER_NOT_FOUND.getCode())) // MEMBER_1001 코드 확인
                    .andExpect(jsonPath("$.message").value(ErrorStatus.MEMBER_NOT_FOUND.getMessage())); // 실패 메시지 확인
        }
    }

    @Nested
    @DisplayName("사용자 정보 조회 메서드 테스트")
    class GetUserInfoTest {
        @Test
        @DisplayName("✅ 사용자 정보 조회 시 사용자 정보를 반환한다.")
        void getUserInfo_ShouldReturnUserInfo() throws Exception {
            // given: 사용자 정보 조회 시 사용자 정보 반환
            String accessToken = jwtTokenProvider.generateToken(member1.getEmail()).getAccessToken();
            given(memberRepository.findMemberWithTechStacksAndProjectsAndTeam(member1.getMemberId()))
                    .willReturn(Optional.of(member1));

            // when & then: 사용자 정보 조회 API 호출 시 사용자 정보 반환
            mockMvc.perform(get("/api/v2/member/info")
                            .header("Authorization", "Bearer " + accessToken))
                    .andDo(print())
                    .andExpect(status().isOk()) // HTTP status 200 OK 응답 검증
                    .andExpect(jsonPath("$.isSuccess").value(true)) // isSuccess 값 검증
                    .andExpect(jsonPath("$.code").value("MEMBER_1000")) // code 값 검증
                    .andExpect(jsonPath("$.message").value("성공입니다.")) // message 값 검증
                    .andExpect(jsonPath("$.result.member_id").value(member1.getMemberId())) // member_id 값 검증
                    .andExpect(jsonPath("$.result.nickname").value(member1.getNickname())) // nickname 값 검증
                    .andExpect(jsonPath("$.result.email").value(member1.getEmail())); // email 값 검증
        }

        @Test
        @DisplayName("❌ 사용자 정보 조회 시 사용자 정보가 없는 경우 예외가 발생한다.")
        void getUserInfo_ShouldReturnError_WhenUserInfoNotFound() throws Exception {
            // given: 사용자 정보 조회 시 사용자 정보가 없는 경우
            String accessToken = jwtTokenProvider.generateToken(member1.getEmail()).getAccessToken();
            given(memberRepository.findMemberWithTechStacksAndProjectsAndTeam(member1.getMemberId()))
                    .willReturn(Optional.empty());

            // when & then: 사용자 정보 조회 시 사용자 정보가 없는 경우 예외 발생
            mockMvc.perform(get("/api/v2/member/info")
                            .header("Authorization", "Bearer " + accessToken))
                    .andDo(print())
                    .andExpect(status().isBadRequest()) // HTTP status 400 Bad Request
                    .andExpect(jsonPath("$.isSuccess").value(false)) // 실패 여부 확인
                    .andExpect(jsonPath("$.code").value(ErrorStatus.MEMBER_NOT_FOUND.getCode())) // MEMBER_1001 코드 확인
                    .andExpect(jsonPath("$.message").value(ErrorStatus.MEMBER_NOT_FOUND.getMessage())); // 실패 메시지 확인
        }
    }
}