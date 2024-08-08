package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.converter.MemberConverter;
import com.codiary.backend.global.converter.PostConverter;
import com.codiary.backend.global.domain.entity.*;
import com.codiary.backend.global.domain.entity.mapping.MemberCategory;
import com.codiary.backend.global.domain.enums.TechStack;
import com.codiary.backend.global.service.MemberService.MemberCommandService;
import com.codiary.backend.global.service.MemberService.MemberQueryService;
import com.codiary.backend.global.web.dto.Member.MemberRequestDTO;
import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;
import com.codiary.backend.global.web.dto.Post.PostResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.service.MemberService.FollowService;
import com.codiary.backend.global.web.dto.Member.FollowResponseDto;
import com.codiary.backend.global.web.dto.Member.MemberSumResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final FollowService followService;
    private final MemberQueryService memberQueryService;
    private final MemberConverter memberConverter;

    @PostMapping("/sign-up")
    @Operation(
            summary = "회원가입"
    )
    public ApiResponse<String> signUp(@Valid @RequestBody MemberRequestDTO.MemberSignUpRequestDTO request) {
        return memberCommandService.signUp(request);
    }

    @PostMapping("/login")
    @Operation(
            summary = "로그인"
    )
    public ApiResponse<MemberResponseDTO.MemberTokenResponseDTO> login(@Valid @RequestBody MemberRequestDTO.MemberLoginRequestDTO request) {
        return memberCommandService.login(request);
    }


    @Operation(
            summary = "팔로우 및 취소 기능",
            description = "id를 가진 유저에 대해 팔로우하거나 취소할 수 있습니다."
    )
    @PostMapping("/follow/{memberId}")
    public ApiResponse<FollowResponseDto> follow(@PathVariable("memberId") Long toId) {
        Member fromMember = memberCommandService.getRequester();
        Follow follow = followService.follow(toId, fromMember);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toManageFollowDto(follow));
    }

    @Operation(
            summary = "팔로우 여부 조회 기능",
            description = "id를 가진 유저에 대해 팔로우 했는지 여부를 확인할 수 있습니다. true-팔로우O / false-팔로우X"
    )
    @GetMapping("/follow/{memberId}")
    public ApiResponse<Boolean> isFollowing(@PathVariable("memberId") Long toId) {
        Member member = memberCommandService.getRequester();
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, followService.isFollowing(toId, member));
    }

    @Operation(
            summary = "유저가 팔로우한 팔로잉 목록 조회",
            description = "로그인한 유저가 팔로우한 팔로잉 목록 조회"
    )
    @GetMapping("/following")
    public ApiResponse<List<MemberSumResponseDto>> getFollowings() {
        Member member = memberCommandService.getRequester();
        List<Member> followings = followService.getFollowings(member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toFollowingResponseDto(followings));
    }

    @Operation(
            summary = "유저를 팔로우한 팔로워 목록 조회",
            description = "로그인한 유저를 팔로우한 팔로워 목록 조회"
    )
    @GetMapping("/follower")
    public ApiResponse<List<MemberSumResponseDto>> getFollowers() {
        Member member = memberCommandService.getRequester();
        List<Member> followers = followService.getFollowers(member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toFollowerResponseDto(followers));
    }


    @Operation(
            summary = "유저가 작성한 다이어리 목록 카테고리별 조회(페이지네이션)",
            description = "category와 page를 입력해주세요."
    )
    @GetMapping("/posts")
    public ApiResponse<PostResponseDTO.MemberPostPreviewListDTO> getMyDiaries(@RequestParam(value = "category", required = false) String category, @PageableDefault(size=6) Pageable pageable){
        Member member = memberCommandService.getRequester();
        Page<Post> posts = memberQueryService.getMyPosts(member, category, pageable);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, PostConverter.toMemberPostPreviewListDTO(posts));
    }



    // 회원별 북마크 리스트 조회
    @GetMapping("/bookmarks/list/{memberId}")
    @Operation(
            summary = "회원별 북마크 리스트 조회 API",
            description = "bookmarks, memberId"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOOKMARK6000", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "memberId", description = "회원 고유번호, path variable 입니다!")
    })
    public ApiResponse<MemberResponseDTO.BookmarkListDTO> getBookmarkList(
            @PathVariable(name = "memberId") Long memberId,
            @RequestParam(name = "page") Integer page
    ) {

        Page<Bookmark> bookmarkList = memberQueryService.getBookmarkList(memberId, page);

        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toBookmarkListDTO(bookmarkList));

    }

    // 회원별 관심 카테고리탭 리스트 조회
    @GetMapping("/membercategories/list/{memberId}")
    @Operation(
            summary = "회원별 관심 카테고리탭 리스트 조회 API",
            description = "membercategories, memberId"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBERCATEGORY8000", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "memberId", description = "회원 고유번호, path variable 입니다!")
    })
    public ApiResponse<MemberResponseDTO.MemberCategoryListDTO> getMemberCategoryList(
            @PathVariable(name = "memberId") Long memberId
    ) {

        List<MemberCategory> memberCategoryList = memberCommandService.getMemberCategoryList(memberId);

        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toMemberCategoryListDTO(memberCategoryList));

    }

    @PatchMapping(path = "/profile-image", consumes = "multipart/form-data")
    @Operation(
            summary = "프로필 사진 설정"
    )
    public ApiResponse<MemberResponseDTO.MemberImageDTO> updateProfileImage(@ModelAttribute MemberRequestDTO.MemberProfileImageRequestDTO request) {
        Member member = memberCommandService.getRequester();

        return memberCommandService.updateProfileImage(member, request);
    }

    @DeleteMapping("/profile-image")
    @Operation(summary = "프로필 사진 삭제")
    public ApiResponse<String> deleteProflieImage() {
        Member member = memberCommandService.getRequester();
        return memberCommandService.deleteProfileImage(member);
    }

    @GetMapping("/{memberId}/profile-image")
    @Operation(summary = "사용자 프로필 사진 조회")
    public ApiResponse<MemberResponseDTO.MemberImageDTO> getProfileImage(@PathVariable Long memberId) {
        return memberQueryService.getProfileImage(memberId);
    }

    @GetMapping(path = "/profile/{memberId}")
    @Operation(summary = "사용자 프로필 기본 정보 조회", description = "마이페이지 사용자 정보 조회 기능")
    public ApiResponse<MemberResponseDTO.UserProfileDTO> getUserProfile(@PathVariable(value = "memberId") Long memberId){
        Member member = memberCommandService.getRequester();
        Member user = memberQueryService.getUserProfile(memberId);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toProfileResponseDto(member, user));
    }

    @GetMapping(path = "/info")
    @Operation(summary = "사용자 정보 조회", description = "마이페이지에서 생년월일/내 소개/소셜계정 수정 시 조회")
    public ApiResponse<MemberResponseDTO.UserInfoDTO> updateUserInfo(){
        Member member = memberCommandService.getRequester();
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toMemberInfoResponseDto(member));
    }

    @PutMapping(path = "/info")
    @Operation(summary = "사용자 정보 수정", description = "마이페이지에서 생년월일/내 소개/소셜계정 수정 적용")
    public ApiResponse<MemberResponseDTO.UserInfoDTO> updateUserInfo(@Valid @RequestBody MemberRequestDTO.MemberInfoRequestDTO request){
        Member member = memberCommandService.getRequester();
        member = memberCommandService.updateMemberInfo(member, request);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, MemberConverter.toMemberInfoResponseDto(member));
    }

    @PostMapping(path = "/techstack/{techstackName}")
    @Operation(summary = "기술 스택 추가하기", description = "기술 스택 하나씩 추가")
    public ApiResponse<MemberResponseDTO.TechStacksDTO> setTechStacks(@PathVariable(value = "techstackName") TechStack techstack) {
        Member member = memberCommandService.getRequester();
        member = memberCommandService.setTechStacks(member.getMemberId(), techstack);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, memberConverter.toTechStacksResponseDto(member));
    }

    @PostMapping(path = "/project/{projectName}")
    @Operation(summary = "프로젝트 추가하기", description = "프로젝트 하나씩 추가")
    public ApiResponse<?> setProjects(@PathVariable(value = "projectName") String projectName) {
        Member member = memberCommandService.getRequester();
        memberCommandService.setProjects(member.getMemberId(), projectName);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, memberCommandService.setProjects(member.getMemberId(), projectName));
    }
}
