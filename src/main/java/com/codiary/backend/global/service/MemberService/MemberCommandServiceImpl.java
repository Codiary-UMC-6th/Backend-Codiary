package com.codiary.backend.global.service.MemberService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.MemberImage;
import com.codiary.backend.global.domain.entity.Project;
import com.codiary.backend.global.domain.entity.Uuid;
import com.codiary.backend.global.domain.entity.mapping.MemberCategory;
import com.codiary.backend.global.domain.entity.mapping.MemberProjectMap;
import com.codiary.backend.global.domain.entity.mapping.TechStacks;
import com.codiary.backend.global.domain.enums.TechStack;
import com.codiary.backend.global.jwt.JwtTokenProvider;
import com.codiary.backend.global.jwt.SecurityUtil;
import com.codiary.backend.global.jwt.TokenInfo;
import com.codiary.backend.global.repository.*;
import com.codiary.backend.global.s3.AmazonS3Manager;
import com.codiary.backend.global.web.dto.Member.MemberRequestDTO;
import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberCategoryRepository memberCategoryRepository;
    private final UuidRepository uuidRepository;
    private final AmazonS3Manager s3Manager;
    private final MemberImageRepository memberImageRepository;
    private final ProjectRepository projectRepository;
    private final MemberProjectMapRepository memberProjectMapRepository;


    @Override
    public ApiResponse<String> signUp(MemberRequestDTO.MemberSignUpRequestDTO signUpRequest) {
        signUpRequest.isCorrect();

        if (memberRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_EXISTS);
        }

        Member member = Member.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .nickname(signUpRequest.getNickname())
                .birth(signUpRequest.getBirth().toString())
                .gender(Member.Gender.Female)
                .github(signUpRequest.getGithub())
                .linkedin(signUpRequest.getLinkedin())
                .build();
        memberRepository.save(member);

        return ApiResponse.of(SuccessStatus.MEMBER_OK, "정상적으로 가입되었습니다.");
    }

    @Override
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> login(MemberRequestDTO.MemberLoginRequestDTO loginRequest) {

        try {
            // 1. Login ID/PW를 기반으로 Authentication 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();

            // 2. 비밀번호 체크
            // loadUserByUsername method 실행됨
            Authentication authentication = authenticationManagerBuilder.getObject()
                    .authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
            Member member = memberRepository.findByEmail(loginRequest.getEmail()).orElseThrow();

            return ApiResponse.of(SuccessStatus.MEMBER_OK, MemberResponseDTO.MemberTokenResponseDTO.builder()
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .tokenInfo(tokenInfo)
                    .build());
        } catch (AuthenticationException e) {
            throw new MemberHandler(ErrorStatus.MEMBER_LOGIN_FAIL);
        }
    }

    @Override
    public Member getRequester() {
        String userEmail = SecurityUtil.getCurrentMemberEmail();
        System.out.println(userEmail);

        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return member;
    }



    // 회원별 관심 카테고리탭 리스트 조회
    @Override
    public List<MemberCategory> getMemberCategoryList(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<MemberCategory> memberCategoryList = memberCategoryRepository.findAllByMember(member);

        return memberCategoryList;

    }

    @Override
    public ApiResponse<MemberResponseDTO.MemberImageDTO> setProfileImage(Member member, MemberRequestDTO.MemberProfileImageRequestDTO request) {
        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
        String fileUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedUuid), request.getImage());

        MemberImage memberImage = MemberImage.builder()
                .imageUrl(fileUrl)
                .member(member)
                .build();

        MemberImage savedImage = memberImageRepository.save(memberImage);
        MemberResponseDTO.MemberImageDTO response = new MemberResponseDTO.MemberImageDTO(savedImage.getImageUrl());
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, response);
    }

    @Override
    public Member setTechStacks(Long memberId, TechStack techstack) {
        Member member = memberRepository.findMemberWithTechStacks(memberId);
        if (member == null) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        List<TechStacks> techStackList = member.getTechStackList();
        if (techStackList == null) {
            techStackList = new ArrayList<>();
        }

        TechStacks newTechStack = new TechStacks(techstack, member);
        techStackList.add(newTechStack);

        member.setTechStackList(techStackList);

        memberRepository.save(member);

        return member;
    }

    @Override
    public MemberResponseDTO.ProjectsDTO setProjects(Long memberId, String projectName){
        Member member = memberRepository.findMemberWithProjects(memberId);
        if (member == null) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        Project project = Project.builder()
                .projectName(projectName)
                .build();

        projectRepository.save(project);

        MemberProjectMap memberProjectMap = MemberProjectMap.builder()
                .member(member)
                .project(project)
                .build();
        memberProjectMapRepository.save(memberProjectMap);

        List<String> projectList = member.getMemberProjectMapList()
                .stream()
                .map(map -> map.getProject().getProjectName())
                .collect(Collectors.toList());

        return MemberResponseDTO.ProjectsDTO.builder()
                .memberId(member.getMemberId())
                .projectList(projectList)
                .build();
    }

    @Override
    public Member updateMemberInfo(Member member, MemberRequestDTO.MemberInfoRequestDTO request){
        Member updatedMember = member.toBuilder()
                .birth(request.getBirth())
                .introduction(request.getIntroduction())
                .github(request.getGithub())
                .linkedin(request.getLinkedin())
                .discord(request.getDiscord())
                .build();

        return memberRepository.save(updatedMember);
    }
}
