package com.codiary.backend.domain.post.controller;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.security.CustomMemberDetails;
import com.codiary.backend.domain.member.service.MemberCommandService;
import com.codiary.backend.domain.post.converter.PostConverter;
import com.codiary.backend.domain.post.dto.request.PostRequestDTO;
import com.codiary.backend.domain.post.dto.response.PostResponseDTO;
import com.codiary.backend.domain.post.entity.Bookmark;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.service.BookmarkService;
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
import java.util.Optional;
import java.util.Set;
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
import org.springframework.web.bind.annotation.RequestBody;
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
    private final BookmarkService bookmarkService;

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
    @Operation(summary = "게시글 수정 API", description = "게시글을 수정합니다.")
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> updatePost(
            @ModelAttribute PostRequestDTO.UpdatePostDTO request,
            @PathVariable Long postId
    ) {
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        return ApiResponse.onSuccess(SuccessStatus.POST_OK,
                PostConverter.toUpdatePostResultDTO(postCommandService.updatePost(postId, request)));
    }


    // 게시글 삭제하기
    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제 API", description = "게시글을 삭제합니다.")
    public ApiResponse<?> deletePost(@PathVariable Long postId) {
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        postCommandService.deletePost(postId);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, null);
    }

    // 저자의 게시글 리스트 페이징 조회
    @GetMapping("/member/{memberId}/paging")
    @Operation(summary = "저자의 게시글 리스트 페이징 조회 API", description = "저자의 게시글 리스트를 페이징으로 조회하기 위해 'Path Variable'로 해당 팀의 'memberId'를 받습니다. **첫 페이지는 0부터 입니다.**", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.MemberPostPreviewListDTO> findPostByMember(@PathVariable Long memberId, @RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(5) Integer size) {
        Page<Post> posts = postQueryService.getPostsByMember(memberId, page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toMemberPostPreviewListDTO(posts));
    }


    // 팀의 게시글 리스트 페이징 조회
    @GetMapping("/team/{teamId}/paging")
    @Operation(summary = "팀의 게시글 리스트 페이징 조회 API", description = "팀의 게시글 리스트를 페이징으로 조회하기 위해 'Path Variable'로 해당 팀의 'teamId'를 받습니다. **첫 페이지는 0부터 입니다.**", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.TeamPostPreviewListDTO> findPostByTeam(@PathVariable Long teamId, @RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(6) Integer size){
        Page<Post> posts = postQueryService.getPostsByTeam(teamId, page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toTeamPostPreviewListDTO(posts));
    }


    // 프로젝트별 저자의 게시글 리스트 페이징 조회
    @GetMapping("/project/{projectId}/member/{memberId}/paging")
    @Operation(summary = "프로젝트별 저자의 게시글 리스트 페이징 조회 API", description = "프로젝트별 저자의 게시글 리스트를 페이징으로 조회하기 위해 'Path Variable'로 해당 프로젝트의 'projectId'와 저자의 'memberId'를 받습니다. **첫 페이지는 0부터 입니다.**", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.MemberPostInProjectPreviewListDTO> findPostByMemberInProject(@PathVariable Long projectId, @PathVariable Long memberId, @RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(5) Integer size){
        Page<Post> posts = postQueryService.getPostsByMemberInProject(projectId, memberId, page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toMemberPostInProjectPreviewListDTO(posts));
    }


    // 프로젝트별 팀의 게시글 리스트 페이징 조회
    @GetMapping("/project/{projectId}/team/{teamId}/paging")
    @Operation(summary = "프로젝트별 팀의 게시글 리스트 페이징 조회 API", description = "프로젝트별 팀의 게시글 리스트를 페이징으로 조회하기 위해 'Path Variable'로 해당 프로젝트의 'projectId'와 팀의 'teamId'를 받습니다. **첫 페이지는 0부터 입니다.**", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.TeamPostInProjectPreviewListDTO> findPostByTeamInProject(@PathVariable Long projectId, @PathVariable Long teamId, @RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(6) Integer size){
        Page<Post> posts = postQueryService.getPostsByTeamInProject(projectId, teamId, page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toTeamPostInProjectPreviewListDTO(posts));
    }

    // 팀별 저자의 게시글 리스트 페이징 조회
    @GetMapping("/team/{teamId}/member/{memberId}/paging")
    @Operation(summary = "팀별 저자의 게시글 리스트 페이징 조회 API", description = "팀별 저자의 게시글 리스트를 페이징으로 조회하기 위해 'Path Variable'로 해당 팀의 'teamId'와 저자의 'memberId'를 받습니다. **첫 페이지는 0부터 입니다.**", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.MemberPostInTeamPreviewListDTO> findPostByMemberInTeam(@PathVariable Long teamId, @PathVariable Long memberId, @RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(6) Integer size){
        Page<Post> posts = postQueryService.getPostsByMemberInTeam(teamId, memberId, page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toMemberPostInTeamPreviewListDTO(posts));
    }

    // 제목으로 게시글 리스트 페이징 조회
    @GetMapping("/title/paging")
    @Operation(summary = "제목으로 게시글 리스트 페이징 조회 API", description = "제목으로 게시글 리스트를 페이징으로 조회합니다. Param으로 제목을 입력하세요.", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.PostPreviewListDTO> findPostsByTitle(@RequestParam Optional<String> search, @RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(9) Integer size) {
        Page<Post> posts = postQueryService.getPostsByTitle(Optional.of(search.orElse("")), page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostPreviewListDTO(posts));
    }


//    // 카테고리명으로 게시글 리스트 페이징 조회
//    @GetMapping("/categories/paging")
//    @Operation(summary = "카테고리명으로 게시글 리스트 페이징 조회 API", description = "카테고리명으로 게시글 리스트를 페이징 조회합니다. 입력한 카테고리가 포함된 모든 다이어리를 조회할 수 있습니다. Param으로 카테고리를 입력하세요.", security = @SecurityRequirement(name = "accessToken"))
//    public ApiResponse<PostResponseDTO.PostPreviewListDTO> findPostsByCategoryName(@RequestParam Optional<String> search, @RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(9) Integer size){
//        Page<Post> posts = postQueryService.getPostsByCategories(Optional.of(search.orElse("")), page, size);
//        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostPreviewListDTO(posts));
//    }


    // 인접한 게시글 조회 (이전 게시글, 다음 게시글)
    @GetMapping("/{postId}/adjacent")
    @Operation(summary = "인접한 게시글 조회 API", description = "특정 게시글의 인접한 게시글을 조회합니다.", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.PostAdjacentDTO> findAdjacentPosts(@PathVariable Long postId){
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostAdjacentDTO(postQueryService.findAdjacentPosts(postId)));
    }

    // 전체 인기글 or 최신글 조회
    @Operation(summary = "공개글 리스트 조회", description = "popular/latest 입력 시 인기글/최신글 조회")
    @GetMapping("/list")
    public ApiResponse<Page<PostResponseDTO.SimplePostResponseDTO>> getPostList(
            @PageableDefault(size = 9) Pageable pageable
    ) {
        Page<Post> postPage = postService.getPostList(pageable);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostListResponseDto(postPage));
    }

    // 카테고리 인기글 조회
    @Operation(summary = "카테고리 인기글/최신글 조회 (popular/latest 입력 (기본 popular)")
    @GetMapping("/category/{category_id}")
    public ApiResponse<Page<PostResponseDTO.SimplePostResponseDTO>> getCategoryPopularPosts(
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @PathVariable("category_id") Long categoryId,
            @PageableDefault(size = 9, sort = "popular") Pageable pageable
    ) {
        Long memberId = memberDetails.getId();
        Page<Post> postPage = postService.getCategoryPosts(memberId, categoryId, pageable);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostListResponseDto(postPage));
    }

//    // 팔로잉 게시글 리스트 조회
//    @Operation(summary = "팔로잉 멤버 게시글 조회")
//    @GetMapping("/following")
//    public ApiResponse<Page<PostResponseDTO.SimplePostResponseDTO>> getFollowingMemberPosts(
//            @AuthenticationPrincipal CustomMemberDetails memberDetails,
//            @PageableDefault(size = 9) Pageable pageable
//    ) {
//        Page<Post> postPage = postService.getFollowingMemberPosts(memberDetails.getId(), pageable);
//        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostListResponseDto(postPage));
//    }


    // 게시글 검색 결과 페이지네이션
    @Operation(summary = "게시글 검색 결과 페이지네이션", description = "게시글(제목/내용) 키워드 검색 결과를 페이지네이션하여 반환합니다.")
    @GetMapping("/search")
    public ApiResponse<Page<PostResponseDTO.SimplePostResponseDTO>> searchPost(
            @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @PageableDefault(size = 9) Pageable pageable
    ) {
        Long memberId = (memberDetails == null) ? 0 : memberDetails.getId();
        Page<Post> postPage = postService.searchPost(memberId, keyword, pageable);

        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostListResponseDto(postPage));
    }


    // 게시글의 카테고리 설정 및 변경
    @PatchMapping("/category/{postId}")
    @Operation(summary = "게시글의 카테고리 설정 및 변경 API", description = "게시글의 카테고리를 설정 및 변경합니다.")
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> setPostCategory(
            @PathVariable Long postId,
            @RequestBody Set<String> categoryNames
    ) {
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        Post updatedPost = postCommandService.setPostCategories(postId, categoryNames);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toSetPostCategoriesResultDTO(updatedPost));
    }

    // 게시글 검색 (저자 이름, 팀 이름, 프로젝트 이름으로 검색)
    @GetMapping("/search_by_name")
    @Operation(summary = "게시글 검색 (저자 이름, 팀 이름, 프로젝트 이름으로 검색)")
    public ApiResponse<Page<PostResponseDTO.SimplePostResponseDTO>> searchByName(
            @RequestParam(value = "author", defaultValue = "", required = false) String authorName,
            @RequestParam(value = "team", defaultValue = "", required = false) String teamName,
            @RequestParam(value = "project", defaultValue = "", required = false) String projectName,
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @PageableDefault(size = 9) Pageable pageable
    ) {
        Long memberId = memberDetails != null ? memberDetails.getId() : 0;
        Page<Post> postPage = postService.searchPostsByName(memberId, authorName, teamName, projectName, pageable);

        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostListResponseDto(postPage));
    }

    // 북마크 추가
    @PostMapping("/{post_id}/bookmark")
    @Operation(summary = "게시글 북마크")
    public ApiResponse<PostResponseDTO.BookmarkDTO> bookmarkPost(
            @PathVariable("post_id") Long postId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        Bookmark bookmark = bookmarkService.bookmarkPost(memberId, postId);

        return ApiResponse.onSuccess(SuccessStatus.BOOKMARK_OK, PostConverter.toBookmarkDTO(bookmark));
    }

    // 북마크 삭제
    @DeleteMapping("/{post_id}/bookmark")
    @Operation(summary = "게시글 북마크 취소")
    public ApiResponse<String> cancelBookmark(
            @PathVariable("post_id") Long postId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getId();
        String response = bookmarkService.cancelBookmark(memberId, postId);

        return ApiResponse.onSuccess(SuccessStatus.BOOKMARK_OK, response);
    }

    @GetMapping("/following/paging")
    @Operation(summary = "팔로잉한 멤버/팀의 게시글 리스트 페이징 조회 API", description = "팔로잉한 멤버/팀의 게시글 리스트를 페이징으로 조회합니다. **첫 페이지는 0부터 입니다.**")
    public ApiResponse<PostResponseDTO.PostPreviewListDTO> findPostByFollowing(@AuthenticationPrincipal CustomMemberDetails memberDetails,
                                                                               @PageableDefault(size = 9) Pageable pageable) {
        Page<Post> posts = postQueryService.getPostsByFollowing(memberDetails.getId(), pageable);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostPreviewListDTO(posts));
    }

    @GetMapping("/bookmark/paging")
    @Operation(summary = "북마크한 게시글 조회")
    public ApiResponse<Page<PostResponseDTO.SimplePostResponseDTO>> getBookmarkPost(@AuthenticationPrincipal CustomMemberDetails memberDetails,
                                                                                    @PageableDefault(size = 9) Pageable pageable) {
        return ApiResponse.onSuccess(SuccessStatus.POST_OK,
                PostConverter.toPostListResponseDto(postQueryService.getBookmarkPost(memberDetails.getId(), pageable)));
    }
}
