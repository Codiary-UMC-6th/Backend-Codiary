package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.converter.BookmarkConverter;
import com.codiary.backend.global.converter.MemberConverter;
import com.codiary.backend.global.converter.PostConverter;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.Bookmark;
import com.codiary.backend.global.domain.entity.mapping.MemberCategory;
import com.codiary.backend.global.service.MemberService.MemberCommandService;
import com.codiary.backend.global.service.MemberService.MemberCommandServiceImpl;
import com.codiary.backend.global.service.MemberService.MemberQueryService;
import com.codiary.backend.global.web.dto.Bookmark.BookmarkResponseDTO;
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


    //TODO: 로그인 구현 완료 시 principal 추가(follow 주체) 필요
    @PostMapping("/follow/{id}")
    public ApiResponse<FollowResponseDto> follow(@PathVariable("id") Long toId) {
        Member member = memberCommandService.getRequester();
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, followService.follow(toId, member));
    }

    //TODO: 로그인 구현 완료 시 principal 추가(follow 주체) 필요
    @GetMapping("/follow/{id}")
    public ApiResponse<Boolean> isFollowing(@PathVariable("id") Long toId) {
        Member member = memberCommandService.getRequester();
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, followService.isFollowing(toId, member));
    }

    //TODO: 로그인 구현 완료 시 principal 추가(follow 주체) 필요
    @GetMapping("/following")
    public ApiResponse<List<MemberSumResponseDto>> getFollowings() {
        Member member = memberCommandService.getRequester();
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, followService.getFollowings(member));
    }

    //TODO: 로그인 구현 완료 시 principal 추가(follow 주체) 필요
    @GetMapping("/follower")
    public ApiResponse<List<MemberSumResponseDto>> getFollowers() {
        Member member = memberCommandService.getRequester();
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, followService.getFollowers(member));
    }


    @GetMapping("/posts")
    public ApiResponse<PostResponseDTO.MemberPostPreviewListDTO> getMyDiaries(@PageableDefault(size=6) Pageable pageable){
        Member member = memberCommandService.getRequester();
        Page<Post> posts = memberQueryService.getMyPosts(member, pageable);
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

}
