package com.codiary.backend.domain.member.service;

import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.repository.PostRepository;
import com.codiary.backend.domain.project.entity.Project;
import com.codiary.backend.domain.project.repository.ProjectRepository;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService{
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final PostRepository postRepository;

    public ApiResponse<MemberResponseDTO.MemberImageDTO> getProfileImage(Long memberId) {
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, memberRepository.findProfileImageUrl(memberId));
    }

    public Member getUserProfile(Long memberId) {
        Member user = memberRepository.findMemberWithTechStacksAndProjectsAndTeam(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        return user;
    }

    public Member getUserInfo(Long memberId) {
        Member user = memberRepository.findMemberWithTechStacksAndProjectsAndTeam(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        return user;
    }

    public Map<LocalDate, List<Project>> getProjectsByMonth(Long memberId, YearMonth yearMonth) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        return projectRepository.findProjectsForCalendar(member.getMemberId(), yearMonth.atDay(1), yearMonth.atEndOfMonth());
    }

    public Map<Project, List<Post>> getPostsByDay(Long memberId, LocalDate date) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        return postRepository.findPostsForCalendar(member.getMemberId(), date);
    }

}
