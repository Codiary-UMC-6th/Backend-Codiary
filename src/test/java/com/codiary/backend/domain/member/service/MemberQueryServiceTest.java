package com.codiary.backend.domain.member.service;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.member.util.MemberUtilTest;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("MemberQueryService 유닛테스트")
@ExtendWith(MockitoExtension.class)
public class MemberQueryServiceTest {
    @InjectMocks
    private MemberQueryService memberQueryService;

    @Mock
    private MemberRepository memberRepository;

    private final Member member1 = MemberUtilTest.createMember1();
    private final Member member2 = MemberUtilTest.createMember2();

    @Nested
    @DisplayName("사용자 기본 정보 조회 메서드 테스트")
    class GetUserInfoTest {

        @Test
        @DisplayName("✅ memberId가 주어지면 해당 사용자를 반환한다.")
        void shouldReturnMemberWhenValidIdGiven() {
            // given
            Long memberId = member1.getMemberId();
            given(memberRepository.findMemberWithTechStacksAndProjectsAndTeam(memberId))
                    .willReturn(Optional.of(member1)); // Member1 반환하도록 Mocking

            // when
            Member result = memberQueryService.getUserInfo(memberId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getMemberId()).isEqualTo(memberId); // 반환된 Member의 memberId가 주어진 memberId와 같은지 확인
            verify(memberRepository, times(1))
                    .findMemberWithTechStacksAndProjectsAndTeam(memberId); // memberRepository의 메서드가 1번 호출되었는지 확인
        }

        @Test
        @DisplayName("❌ 존재하지 않는 memberId를 조회하면 예외가 발생한다.")
        void shouldThrowExceptionWhenMemberNotFound() {
            // given
            Long invalidMemberId = 999L;
            given(memberRepository.findMemberWithTechStacksAndProjectsAndTeam(invalidMemberId))
                    .willReturn(Optional.empty()); // 빈 Optional 반환하도록 Mocking

            // when & then
            assertThatThrownBy(() -> memberQueryService.getUserInfo(invalidMemberId)) // 예외 발생 여부 확인
                    .isInstanceOf(GeneralException.class)
                    .hasMessageContaining(ErrorStatus.MEMBER_NOT_FOUND.getMessage());

            verify(memberRepository, times(1))
                    .findMemberWithTechStacksAndProjectsAndTeam(invalidMemberId); // memberRepository의 메서드가 1번 호출되었는지 확인
        }
    }
}