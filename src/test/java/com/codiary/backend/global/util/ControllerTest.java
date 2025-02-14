package com.codiary.backend.global.util;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.member.security.CustomMemberDetails;
import com.codiary.backend.domain.member.security.CustomMemberDetailsService;
import com.codiary.backend.domain.member.service.MemberQueryService;
import com.codiary.backend.domain.member.util.MemberUtilTest;
import com.codiary.backend.global.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@AutoConfigureMockMvc
public abstract class ControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    public MemberQueryService memberQueryService;

    @Autowired
    public JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected MemberRepository memberRepository;

    @MockBean
    protected CustomMemberDetailsService customMemberDetailsService;

    public Member member1;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .build();

        // member1 생성 및 설정
        member1 = MemberUtilTest.createMember1();
        member1.setMemberId(1L);

        // `MemberRepository` 모킹하여 member1의 이메일에 해당하는 사용자를 반환하도록 설정
        given(memberRepository.findByEmail(member1.getEmail())).willReturn(Optional.of(member1));

        // `CustomMemberDetails` 생성 및 모킹
        CustomMemberDetails customMemberDetails = new CustomMemberDetails(
                member1.getEmail(), member1.getMemberId(), member1.getPassword(), null);
        given(customMemberDetailsService.loadUserByUsername(anyString()))
                .willReturn(customMemberDetails);

        // SecurityContext에 CustomMemberDetails 설정
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(
                customMemberDetails, null, customMemberDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);
    }
}