package com.codiary.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codiary.backend.global.jwt.JwtTokenProvider;
import com.codiary.backend.global.jwt.TokenInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String token;

    @BeforeEach
    public void setup() {
        // 존재하는 사용자 정보를 바탕으로 토큰을 생성합니다. DB에 있는 사용자의 email을 입력해주어야 해요.
        TokenInfo token = jwtTokenProvider.generateToken("string@123.123");
        this.token = token.getAccessToken();
    }

    @Test
    public void test() throws Exception {
        // Test 하고자 하는 url을 입력
        MvcResult result = mockMvc.perform(
                get("/api/v2/member/info").header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andReturn();

        // 응답 분석 (body든 뭐든 정보 꺼내서 확인해봅니다.)
        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("\"email\":\"string@123.123\"");
    }
}
