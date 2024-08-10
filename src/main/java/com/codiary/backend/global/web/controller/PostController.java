package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.PostConverter;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.jwt.JwtTokenProvider;
import com.codiary.backend.global.repository.ProjectRepository;
import com.codiary.backend.global.repository.TeamRepository;
import com.codiary.backend.global.service.MemberService.MemberCommandService;
import com.codiary.backend.global.service.PostService.PostCommandService;
import com.codiary.backend.global.service.PostService.PostQueryService;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;
import com.codiary.backend.global.web.dto.Post.PostResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RequestMapping("/posts")
@Slf4j
public class PostController {
    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;
    private final MemberCommandService memberCommandService;
    private final JwtTokenProvider jwtTokenProvider;

    // 다이어리 생성하기
    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "다이어리 생성 API", description = "다이어리를 생성합니다. **카테고리 설정은 다이어리 생성과는 별도로 설정해야 됩니다.**")
    public ApiResponse<PostResponseDTO.CreatePostResultDTO> createPost(@ModelAttribute PostRequestDTO.CreatePostRequestDTO request) {
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        //Post newPost = postCommandService.createPost(request);
        Post newPost = postCommandService.createPost(request);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toCreateResultDTO(newPost));
    }


    // 멤버의 다이어리 수정하기
    @PatchMapping(path = "/{postId}", consumes = "multipart/form-data")
    @Operation(summary = "다이어리 수정 API", description = "다이어리를 수정합니다.")
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> updatePost(@ModelAttribute PostRequestDTO.UpdatePostDTO request, @PathVariable Long postId){
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toUpdatePostResultDTO(postCommandService.updatePost(postId, request)));
    }


    // 다이어리 삭제하기
    @DeleteMapping("/{postId}")
    @Operation(summary = "다이어리 삭제 API", description = "다이어리를 삭제합니다.")
    public ApiResponse<?> deletePost(@PathVariable Long postId){
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        postCommandService.deletePost(postId);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, null);
    }


    // 다이어리 공개/비공개 설정
    @PatchMapping("/visibility/{postId}")
    @Operation(summary = "다이어리 공개/비공개 설정 API", description = "다이어리의 공개 범위를 설정합니다.")
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> setPostVisibility(@PathVariable Long postId, @RequestBody PostRequestDTO.UpdateVisibilityRequestDTO request) {
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        Post updatedPost = postCommandService.updateVisibility(postId, request);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toUpdatePostResultDTO(updatedPost));
    }


    // 다이어리 공동 저자 설정
    @PatchMapping("/coauthors/{postId}")
    @Operation(summary = "다이어리 공동 저자 설정 API", description = "다이어리의 공동 저자를 설정합니다.")
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> setPostCoauthor(@PathVariable Long postId, @RequestBody PostRequestDTO.UpdateCoauthorRequestDTO request) {
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        Post updatedPost = postCommandService.updateCoauthors(postId, request);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toUpdatePostResultDTO(updatedPost));
    }


    // 다이어리의 소속 팀 설정
    @PatchMapping("/team/{postId}")
    @Operation(summary = "다이어리의 소속 팀 설정 API", description = "다이어리의 소속 팀을 설정합니다.")
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> setPostTeam(@PathVariable Long postId, @RequestBody PostRequestDTO.SetTeamRequestDTO request) {
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        Post updatedPost = postCommandService.setPostTeam(postId, request.getTeamId());
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toUpdatePostResultDTO(updatedPost));
    }


    // 저자의 다이어리 리스트 페이징 조회
    @GetMapping("/member/{memberId}/paging")
    @Operation(summary = "저자의 다이어리 리스트 페이징 조회 API", description = "저자의 다이어리 리스트를 페이징으로 조회하기 위해 'Path Variable'로 해당 팀의 'memberId'를 받습니다. **첫 페이지는 0부터 입니다.**", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.MemberPostPreviewListDTO> findPostByMember(@PathVariable Long memberId, @RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(5) Integer size) {
        Page<Post> posts = postQueryService.getPostsByMember(memberId, page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toMemberPostPreviewListDTO(posts));
    }


    // 팀의 다이어리 리스트 페이징 조회
    @GetMapping("/team/{teamId}/paging")
    @Operation(summary = "팀의 다이어리 리스트 페이징 조회 API", description = "팀의 다이어리 리스트를 페이징으로 조회하기 위해 'Path Variable'로 해당 팀의 'teamId'를 받습니다. **첫 페이지는 0부터 입니다.**", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.TeamPostPreviewListDTO> findPostByTeam(@PathVariable Long teamId, @RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(6) Integer size){
        Page<Post> posts = postQueryService.getPostsByTeam(teamId, page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toTeamPostPreviewListDTO(posts));
    }


    // 프로젝트별 저자의 다이어리 리스트 페이징 조회
    @GetMapping("/project/{projectId}/member/{memberId}/paging")
    @Operation(summary = "프로젝트별 저자의 다이어리 리스트 페이징 조회 API", description = "프로젝트별 저자의 다이어리 리스트를 페이징으로 조회하기 위해 'Path Variable'로 해당 프로젝트의 'projectId'와 저자의 'memberId'를 받습니다. **첫 페이지는 0부터 입니다.**", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.MemberPostInProjectPreviewListDTO> findPostByMemberInProject(@PathVariable Long projectId, @PathVariable Long memberId, @RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(5) Integer size){
        Page<Post> posts = postQueryService.getPostsByMemberInProject(projectId, memberId, page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toMemberPostInProjectPreviewListDTO(posts));
    }


    // 프로젝트별 팀의 다이어리 리스트 페이징 조회
    @GetMapping("/project/{projectId}/team/{teamId}/paging")
    @Operation(summary = "프로젝트별 팀의 다이어리 리스트 페이징 조회 API", description = "프로젝트별 팀의 다이어리 리스트를 페이징으로 조회하기 위해 'Path Variable'로 해당 프로젝트의 'projectId'와 팀의 'teamId'를 받습니다. **첫 페이지는 0부터 입니다.**", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.TeamPostInProjectPreviewListDTO> findPostByTeamInProject(@PathVariable Long projectId, @PathVariable Long teamId, @RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(6) Integer size){
        Page<Post> posts = postQueryService.getPostsByTeamInProject(projectId, teamId, page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toTeamPostInProjectPreviewListDTO(posts));
    }


    // 팀별 저자의 다이어리 리스트 페이징 조회
    @GetMapping("/team/{teamId}/member/{memberId}/paging")
    @Operation(summary = "팀별 저자의 다이어리 리스트 페이징 조회 API", description = "팀별 저자의 다이어리 리스트를 페이징으로 조회하기 위해 'Path Variable'로 해당 팀의 'teamId'와 저자의 'memberId'를 받습니다. **첫 페이지는 0부터 입니다.**", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.MemberPostInTeamPreviewListDTO> findPostByMemberInTeam(@PathVariable Long teamId, @PathVariable Long memberId, @RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(6) Integer size){
        Page<Post> posts = postQueryService.getPostsByMemberInTeam(teamId, memberId, page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toMemberPostInTeamPreviewListDTO(posts));
    }


    // 제목으로 다이어리 리스트 페이징 조회
    @GetMapping("/title/paging")
    @Operation(summary = "제목으로 다이어리 리스트 페이징 조회 API", description = "제목으로 다이어리 리스트를 페이징으로 조회합니다. Param으로 제목을 입력하세요.", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.PostPreviewListDTO> findPostsByTitle(@RequestParam Optional<String> search, @RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(9) Integer size) {
        Page<Post> posts = postQueryService.getPostsByTitle(Optional.of(search.orElse("")), page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostPreviewListDTO(posts));
    }


    // 카테고리명으로 다이어리 리스트 페이징 조회
    @GetMapping("/categories/paging")
    @Operation(summary = "카테고리명으로 다이어리 리스트 페이징 조회 API", description = "카테고리명으로 다이어리 리스트를 페이징 조회합니다. 입력한 카테고리가 포함된 모든 다이어리를 조회할 수 있습니다. Param으로 카테고리를 입력하세요.", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.PostPreviewListDTO> findPostsByCategoryName(@RequestParam Optional<String> search, @RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(9) Integer size){
        Page<Post> posts = postQueryService.getPostsByCategories(Optional.of(search.orElse("")), page, size);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostPreviewListDTO(posts));
    }


    // 인접한 다이어리 조회 (특정 다이어리의 이전, 다음 다이어리 조회)
    @GetMapping("/{postId}/adjacent")
    @Operation(summary = "인접한 다이어리 조회 API", description = "특정 다이어리의 인접한 다이어리를 조회합니다.", security = @SecurityRequirement(name = "accessToken"))
    public ApiResponse<PostResponseDTO.PostAdjacentDTO> findAdjacentPosts(@PathVariable Long postId){
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toPostAdjacentDTO(postQueryService.findAdjacentPosts(postId)));
    }


    // 다이어리의 카테고리 설정 및 변경
    @PatchMapping("/categories/{postId}")
    @Operation(summary = "다이어리의 카테고리 설정 및 변경 API", description = "다이어리의 카테고리를 설정 및 변경합니다.")
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> setPostCategory(@PathVariable Long postId, @RequestBody Set<String> categoryNames){
        Member member = memberCommandService.getRequester();
        jwtTokenProvider.isValidToken(member.getMemberId());

        Post updatedPost = postCommandService.setPostCategories(postId, categoryNames);
        return ApiResponse.onSuccess(SuccessStatus.POST_OK, PostConverter.toSetPostCategoriesResultDTO(updatedPost));
    }


//    // 다이어리 커스터마이징 옵션 변경
//    @PatchMapping("/customize/{postId}")
//    @Operation(summary = "다이어리 커스터마이징 옵션 변경 API", description = "다이어리의 커스터마이징 옵션을 변경합니다."//, security = @SecurityRequirement(name = "accessToken")
//    )
//    public ApiResponse<PostResponseDTO> customizePostOption(
//    ){ return null; }


//    // AI로 코드 실행 미리보기
//    @PostMapping("/code-preview/{postId}")
//    @Operation(summary = "AI로 코드 실행 미리보기 API", description = "AI로 특정 글에 포함된 코드를 실행하여 미리보기 결과를 반환합니다."//, security = @SecurityRequirement(name = "accessToken")
//    )
//    public ApiResponse<PostResponseDTO> showPostCodePreview(
//    ){ return null; }

}
