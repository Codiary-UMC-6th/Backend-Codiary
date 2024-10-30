package com.codiary.backend.domain.team.service;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.team.dto.request.TeamRequestDTO;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.entity.TeamBannerImage;
import com.codiary.backend.domain.team.entity.TeamMember;
import com.codiary.backend.domain.team.entity.TeamProfileImage;
import com.codiary.backend.domain.team.enumerate.TeamMemberRole;
import com.codiary.backend.domain.team.repository.TeamBannerImageRepository;
import com.codiary.backend.domain.team.repository.TeamProfileImageRepository;
import com.codiary.backend.domain.team.repository.TeamRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.GeneralException;
import com.codiary.backend.global.common.uuid.Uuid;
import com.codiary.backend.global.common.uuid.UuidRepository;
import com.codiary.backend.global.s3.AmazonS3Manager;
import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final AmazonS3Manager s3Manager;
    private final TeamBannerImageRepository bannerImageRepository;
    private final TeamProfileImageRepository profileImageRepository;
    private final UuidRepository uuidRepository;

    @Transactional
    public Team createTeam(TeamRequestDTO.CreateTeamDTO request, Long memberId){
        //validation: 팀장 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //business logic: 팀 생성, 팀장 등록
        Team team = Team.builder()
                .name(request.name())
                .intro(request.intro())
                .github(request.github())
                .email(member.getEmail())
                .linkedin(request.linkedIn())
                .instagram(request.instagram())
                .teamMemberList(new ArrayList<>())
                .bannerImage(null)
                .profileImage(null)
                .build();
        addMemberToTeam(member, team, TeamMemberRole.ADMIN);

        //response: 팀 반환
        return teamRepository.save(team);
    }

    public Team getTeamProfile(Long teamId, Long memberId){
        //validation: member 유효성 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //business logic: 팀 조회
        Team team = teamRepository.findTeamProfile(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        //response: team 반환
        return team;
    }

    public Team getTeam(Long teamId, Long memberId){
        //validation: member 유효성 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //business logic: 팀 조회 / 수정 페이지 접근 권환 확인
        Team team = teamRepository.findByTeamIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        if (!teamRepository.isTeamMember(team, member)){
            throw new GeneralException(ErrorStatus.TEAM_ADMIN_UNAUTHORIZED);
        }

        //response: team 반환
        return team;
    }

    @Transactional
    public Team updateTeam(TeamRequestDTO.UpdateTeamDTO request, Long teamId, Long memberId){
        //validation: member 유효성 확인 / team 존재 여부 확인  / 수정 페이지 접근 권환 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Team team = teamRepository.findByTeamIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        if (!teamRepository.isTeamMember(team, member)){
            throw new GeneralException(ErrorStatus.TEAM_ADMIN_UNAUTHORIZED);
        }

        //business logic: team 수정
        team.update(request);

        //response: team 반환
        return teamRepository.save(team);
    }

    private void addMemberToTeam(Member member, Team team, TeamMemberRole role) {
        TeamMember teamMember = TeamMember.builder()
                .member(member)
                .team(team)
                .teamMemberRole(role)
                .build();
        team.getTeamMemberList().add(teamMember);
    }

    @Transactional
    public TeamProfileImage setTeamProfileImage(Long teamId, Long memberId, TeamRequestDTO.TeamImageDTO request) {
        // validation: member 유효성 확인 / team 존재 여부 확인  / 수정 페이지 접근 권환 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Team team = teamRepository.findByTeamIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        if (!teamRepository.isTeamMember(team, member)) {
            throw new GeneralException(ErrorStatus.TEAM_ADMIN_UNAUTHORIZED);
        }

        // business logic: 팀 프로필 이미지 갱신 (기존 이미지 삭제 후 새 이미지 등록)
        if (team.getProfileImage() != null) {
            TeamProfileImage preProfileImage = team.getProfileImage();
            team.setProfileImage(null);
            s3Manager.deleteFile(preProfileImage.getImageUrl());
            profileImageRepository.delete(preProfileImage);
            profileImageRepository.flush();
        }

        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
        String fileUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedUuid), request.image());

        TeamProfileImage profileImage = TeamProfileImage.builder()
                .imageUrl(fileUrl)
                .team(team)
                .build();

        // response: 저장된 이미지 반환
        return profileImageRepository.save(profileImage);
    }

    @Transactional
    public String deleteTeamProfileImage(Long teamId, Long memberId) {
        // validation: member 유효성 확인 / team 존재 여부 확인  / 수정 페이지 접근 권환 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Team team = teamRepository.findByTeamIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        if (!teamRepository.isTeamMember(team, member)) {
            throw new GeneralException(ErrorStatus.TEAM_ADMIN_UNAUTHORIZED);
        }

        // business logic: 팀 프로필 이미지 삭제
        if (team.getProfileImage() != null) {
            s3Manager.deleteFile(team.getProfileImage().getImageUrl());
            profileImageRepository.delete(team.getProfileImage());
            team.setProfileImage(null);
            teamRepository.save(team);
        }

        // response: 성공을 반환
        return "성공적으로 삭제되었습니다!";
    }

    @Transactional
    public TeamBannerImage setTeamBannerImage(Long teamId, Long memberId, TeamRequestDTO.TeamImageDTO request) {
        // validation: member 유효성 확인 / team 존재 여부 확인  / 수정 페이지 접근 권환 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Team team = teamRepository.findByTeamIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        if (!teamRepository.isTeamMember(team, member)) {
            throw new GeneralException(ErrorStatus.TEAM_ADMIN_UNAUTHORIZED);
        }

        // business Logic: 팀 배너 이미지 갱신 (기존 이미지 삭제 후 새 이미지 등록)
        if (team.getBannerImage() != null) {
            TeamBannerImage preBannerImage = team.getBannerImage();
            team.setBannerImage(null);
            s3Manager.deleteFile(preBannerImage.getImageUrl());
            bannerImageRepository.delete(preBannerImage);
            bannerImageRepository.flush();
        }

        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
        String fileUrl = s3Manager.uploadFile(s3Manager.generatePostName(savedUuid), request.image());

        TeamBannerImage bannerImage = TeamBannerImage.builder()
                .imageUrl(fileUrl)
                .team(team)
                .build();

        // response: 저장된 이미지 반환
        return bannerImageRepository.save(bannerImage);
    }

    @Transactional
    public String deleteTeamBannerImage(Long teamId, Long memberId) {
        // validation: member 유효성 확인 / team 존재 여부 확인  / 수정 페이지 접근 권환 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Team team = teamRepository.findByTeamIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        if (!teamRepository.isTeamMember(team, member)) {
            throw new GeneralException(ErrorStatus.TEAM_ADMIN_UNAUTHORIZED);
        }

        // business logic: 팀 배너 이미지 삭제
        if (team.getBannerImage() != null) {
            s3Manager.deleteFile(team.getBannerImage().getImageUrl());
            bannerImageRepository.delete(team.getBannerImage());
            team.setBannerImage(null);
            teamRepository.save(team);
        }

        // response: 성공을 반환
        return "성공적으로 삭제되었습니다!";
    }
}
