package com.codiary.backend.domain.post.controller;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.security.CustomMemberDetails;
import com.codiary.backend.domain.member.service.MemberCommandService;
import com.codiary.backend.domain.post.converter.PostConverter;
import com.codiary.backend.domain.post.dto.request.PostRequestDTO;
import com.codiary.backend.domain.post.dto.response.PostResponseDTO;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.service.PostCommandService;
import com.codiary.backend.domain.post.service.PostQueryService;
import com.codiary.backend.domain.post.service.PostService;
import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v2/post")
@CrossOrigin
@Slf4j
@Tag(name = "게시글 API", description = "게시글 관련 API입니다.")
public class PostController {
    private final PostService postService;
    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;
    private final MemberCommandService memberCommandService;
    private final JwtTokenProvider jwtTokenProvider;

    // 게시글 생성하기
    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "게시글 생성 API", description = "게시글을 생성합니다. **카테고리 설정은 게시글 생성과는 별도로 설정해야 됩니다.**")
    public ApiResponse<PostResponseDTO.CreatePostResultDTO> createPost(
            @ModelAttribute PostRequestDTO.CreatePostRequestDTO request) {
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        //Post newPost = postCommandService.createPost(request);
        Post newPost = postCommandService.createPost(request);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toCreateResultDTO(newPost));
    }

    // 멤버의 게시글 수정하기
    @PatchMapping(path = "/{postId}", consumes = "multipart/form-data")
    @Operation(summary = "다이어리 수정 API", description = "다이어리를 수정합니다.")
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> updatePost(
            @ModelAttribute PostRequestDTO.UpdatePostDTO request, @PathVariable Long postId) {
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        return ApiResponse.onSuccess(SuccessStatus.POST_OK,
                PostConverter.toUpdatePostResultDTO(postCommandService.updatePost(postId, request)));
    }


    // 게시글 삭제하기
    @DeleteMapping("/{postId}")
    @Operation(summary = "다이어리 삭제 API", description = "다이어리를 삭제합니다.")
    public ApiResponse<?> deletePost(@PathVariable Long postId) {
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        postCommandService.deletePost(postId);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, null);
    }

    // 저자의 다이어리 리스트 페이징 조회
    @GetMapping("/member/{memberId}/paging")
    @Operation(summary = "저자의 다이어리 리스트 페이징 조회 API", description = "저자의 다이어리 리스트를 페이징으로 조회하기 위해 'Path Variable'로 해당 팀의 'memberId'를 받습니다. **첫 페이지는 0부터 입니다.**", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.MemberPostPreviewListDTO> findPostByMember(@PathVariable Long memberId,
                                                                                  @RequestParam @Min(0) Integer page,
                                                                                  @RequestParam @Min(1) @Max(5) Integer size) {
        Page<Post> posts = postQueryService.getPostsByMember(memberId, page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toMemberPostPreviewListDTO(posts));
    }


    // 팀의 다이어리 리스트 페이징 조회
    @GetMapping("/team/{teamId}/paging")
    @Operation(summary = "팀의 다이어리 리스트 페이징 조회 API", description = "팀의 다이어리 리스트를 페이징으로 조회하기 위해 'Path Variable'로 해당 팀의 'teamId'를 받습니다. **첫 페이지는 0부터 입니다.**", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.TeamPostPreviewListDTO> findPostByTeam(@PathVariable Long teamId,
                                                                              @RequestParam @Min(0) Integer page,
                                                                              @RequestParam @Min(1) @Max(6) Integer size) {
        Page<Post> posts = postQueryService.getPostsByTeam(teamId, page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toTeamPostPreviewListDTO(posts));
    }

    // 프로젝트별 저자의 다이어리 리스트 페이징 조회

    // 프로젝트별 팀의 다이어리 리스트 페이징 조회

    // 팀별 저자의 다이러리 리스트 페이징 조회

    // 제목으로 다이어리 리스트 페이징 조회

    // 카테고리명으로 다이어리 리스트 페이징 조회

    // 인접한 다이어리 조회 (이전 다이어리, 다음 다이어리)


    // 전체 인기글 조회
    @Operation(summary = "전체 인기글 조회")
    @GetMapping("popular")
    public ApiResponse<?> getPopularPosts(@PageableDefault(size = 9) Pageable pageable) {
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, null);
    }

    // 관심 카테고리 인기글 조회
    @Operation(summary = "관심 카테고리 인기글 조회")
    @GetMapping("popular/{category_id}")
    public ApiResponse<?> getCategoryPopularPosts(
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @PathVariable("category_id") String categoryId,
            @PageableDefault(size = 9) Pageable pageable
    ) {
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, null);
    }

    // 최신글 조회
    @Operation(summary = "최신글 조회")
    @GetMapping("latest")
    public ApiResponse<?> getLatestPosts(@PageableDefault(size = 9) Pageable pageable) {
        Page<Post> postPage = postService.getLatestPosts(pageable);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostListResponseDto(postPage));
    }

    // 팔로잉 게시글 리스트 조회
    @Operation(summary = "팔로잉 멤버 게시글 조회")
    @GetMapping("following")
    public ApiResponse<Page<PostResponseDTO.SimplePostResponseDTO>> getFollowingMemberPosts(
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @PageableDefault(size = 9) Pageable pageable
    ) {
        Page<Post> postPage = postService.getFollowingMemberPosts(memberDetails.getId(), pageable);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostListResponseDto(postPage));
    }


    @Operation(summary = "게시글 검색 결과 페이지네이션", description = "게시글(제목/내용) 키워드 검색 결과를 페이지네이션하여 반환합니다.")
    @GetMapping("/search")
    public ApiResponse<Page<PostResponseDTO.SimplePostResponseDTO>> searchPost(
            @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
            @PageableDefault(size = 9) Pageable pageable) {
        Page<Post> postPage = postService.searchPost(keyword, pageable);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostListResponseDto(postPage));
    }

}
