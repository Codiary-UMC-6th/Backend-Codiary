package com.codiary.backend.domain.member.service;

import com.codiary.backend.domain.member.dto.request.MemberRequestDTO;
import com.codiary.backend.domain.member.dto.response.MemberResponseDTO;
import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.entity.MemberImage;
import com.codiary.backend.domain.member.repository.MemberImageRepository;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.techstack.entity.TechStacks;
import com.codiary.backend.domain.techstack.enumerate.TechStack;
import com.codiary.backend.domain.techstack.repository.TechStackRepository;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.apiPayload.exception.handler.MemberHandler;
import com.codiary.backend.global.common.uuid.Uuid;
import com.codiary.backend.global.common.uuid.UuidRepository;
import com.codiary.backend.global.jwt.SecurityUtil;
import com.codiary.backend.global.s3.AmazonS3Manager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final UuidRepository uuidRepository;
    private final AmazonS3Manager s3Manager;
    private final MemberImageRepository memberImageRepository;
    private final TechStackRepository techStackRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member getRequester() {
        String userEmail = SecurityUtil.getCurrentMemberEmail();
        System.out.println(userEmail);

        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return member;
    }

    @Transactional
    public ApiResponse<MemberResponseDTO.MemberImageDTO> updateProfileImage(Long memberId, MemberRequestDTO.MemberProfileImageRequestDTO request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
        String fileUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedUuid), request.image());

        MemberImage memberImage = null;
        if (member.getImage() == null) {
            memberImage = MemberImage.builder()
                    .imageUrl(fileUrl)
                    .member(member)
                    .build();
        } else {
            memberImage = memberImageRepository.findById(member.getImage().getMemberImageId()).get();
            s3Manager.deleteFile(memberImage.getImageUrl());
            memberImage.setImageUrl(fileUrl);
        }

        MemberImage savedImage = memberImageRepository.save(memberImage);

        MemberResponseDTO.MemberImageDTO response = new MemberResponseDTO.MemberImageDTO(savedImage.getImageUrl());
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, response);
    }

    @Transactional
    public ApiResponse<String> deleteProfileImage(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        if (member.getImage() != null) {
            s3Manager.deleteFile(member.getImage().getImageUrl());
            memberImageRepository.delete(member.getImage());
            member.setImage(null);
            memberRepository.save(member);
        }
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, "성공적으로 삭제되었습니다!");
    }

    @Transactional
    public Member updateMemberInfo(Long memberId, MemberRequestDTO.MemberInfoDTO request){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        String password = request.password() != null ? passwordEncoder.encode(request.password()) : null;
        member.updateInfo(request, password);
        return memberRepository.save(member);
    }

    @Transactional
    public Member addTechStack(Long memberId, TechStack techstack) {
        //validatation: member 존재 여부 확인, 기술스택 중복 확인
        Member member = memberRepository.findMemberWithTechStacks(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (techStackRepository.existsByNameAndMember(techstack, member)) {
            throw new GeneralException(ErrorStatus.TECH_STACK_ALREADY_EXISTS);
        }
        //business logic: 기술스택 추가
        List<TechStacks> techStackList = Optional.of(member.getTechStackList()).orElse(new ArrayList<>());
        TechStacks newTechStack = new TechStacks(techstack, member);
        techStackList.add(newTechStack);

        //response: member 반환
        return member;
    }
}
