package com.codiary.backend.global.service.MemberService;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.codiary.backend.global.domain.entity.*;
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
import java.util.Date;
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
    private final TokenRepository tokenRepository;


    @Override
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

    @Override
    public ApiResponse<String> checkEmailDuplication(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new MemberHandler(ErrorStatus.MEMBER_EMAIL_ALREADY_EXISTS);
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new MemberHandler(ErrorStatus.MEMBER_WRONG_EMAIL);
        }

        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, "사용가능한 이메일입니다!");
    }

    @Override
    public ApiResponse<String> checkNicknameDuplication(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new MemberHandler(ErrorStatus.MEMBER_NICKNAME_ALREADY_EXISTS);
        }
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, "사용가능한 닉네임입니다!");
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

    @Override
    public String logout(String token, Member member) {
        if (!tokenRepository.existsByNotAvailableToken(token)) {
            Date expiryTime = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
            Token tokenEntity = Token.builder()
                    .expiryTime(expiryTime)
                    .notAvailableToken(token)
                    .build();
            tokenRepository.save(tokenEntity);
        }
        return "로그아웃되었습니다.";
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

    @Override
    public ApiResponse<String> deleteProfileImage(Member member) {
        if (member.getImage() != null) {
            s3Manager.deleteFile(member.getImage().getImageUrl());
            memberImageRepository.delete(member.getImage());
            member.setImage(null);
            memberRepository.save(member);
        }
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, "성공적으로 삭제되었습니다!");
    }

    @Override
    public Member setTechStacks(Long memberId, TechStack techstack) {
        Member member = memberRepository.findMemberWithTechStacks(memberId);
        if (member == null) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        if (member.getTechStackList().stream().anyMatch(stack -> stack.getName().equals(techstack))) {
            throw new GeneralException(ErrorStatus.TECH_STACK_ALREADY_EXISTS);
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
        member.getMemberProjectMapList().forEach(map -> map.getProject().getProjectName());
        if (member == null) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        if(member.getMemberProjectMapList().stream().anyMatch(map -> map.getProject().getProjectName().equals(projectName))){
            throw new GeneralException(ErrorStatus.PROJECT_ALREADY_EXISTS);
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
        projectList.add(projectName);

        return MemberResponseDTO.ProjectsDTO.builder()
                .memberId(member.getMemberId())
                .projectList(projectList)
                .build();
    }

    @Override
    public Member updateMemberInfo(Member member, MemberRequestDTO.MemberInfoRequestDTO request){
        Member updatedMember = member.toBuilder()
                .birth(request.birth())
                .introduction(request.introduction())
                .github(request.github())
                .linkedin(request.linkedin())
                .discord(request.discord())
                .build();

        return memberRepository.save(updatedMember);
    }
}
