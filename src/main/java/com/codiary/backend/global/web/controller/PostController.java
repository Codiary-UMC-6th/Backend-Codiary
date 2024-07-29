package com.codiary.backend.global.web.controller;

import com.codiary.backend.global.apiPayload.ApiResponse;
import com.codiary.backend.global.apiPayload.code.status.SuccessStatus;
import com.codiary.backend.global.converter.PostConverter;
import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.service.PostService.PostCommandService;
import com.codiary.backend.global.service.PostService.PostQueryService;
import com.codiary.backend.global.web.dto.Post.PostRequestDTO;
import com.codiary.backend.global.web.dto.Post.PostResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    // 글 생성하기
    // TODO: 현재 teamId=1로 되어 있는 부분 수정 구현 필요
    @PostMapping()
    @Operation(
            summary = "글 생성 API", description = "글을 생성합니다."
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO.CreatePostResultDTO> createPost(
            @RequestParam Long memberId,
            @RequestBody PostRequestDTO.CreatePostRequestDTO request
            ){
        Long teamId = 1L;
        Long projectId = 1L;
        Post newPost = postCommandService.createPost(memberId, teamId, projectId, request);
        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toCreateResultDTO(newPost)
        );
    }


    // 글 수정하기
    @PatchMapping("/{postId}")
    @Operation(
            summary = "글 수정 API", description = "글을 수정합니다. Param으로 memberId를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> updatePost(
            @RequestParam Long memberId,
            @RequestBody PostRequestDTO.UpdatePostDTO request,
            @PathVariable Long postId
    ){
        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toUpdatePostResultDTO(
                        postCommandService.updatePost(memberId, postId, request)
                )
        );
    }

    // 글 삭제하기
    @DeleteMapping("/{postId}")
    @Operation(
            summary = "글 삭제 API", description = "글을 삭제합니다. Param으로 memberId를 입력하세요"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<?> deletePost(
            @RequestParam Long memberId,
            @PathVariable Long postId
    ){
        postCommandService.deletePost(memberId, postId);
        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                null
        );
    }


    // 글 공개/비공개 설정
    @PatchMapping("/visibility/{postId}")
    @Operation(
            summary = "글 공개/비공개 API", description = "글의 공개/비공개 유무를 설정합니다."
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> setPostVisibility(
            @PathVariable Long postId,
            @RequestBody PostRequestDTO.UpdateVisibilityRequestDTO request
    ) {
        Post updatedPost = postCommandService.updateVisibility(postId, request);
        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toUpdatePostResultDTO(updatedPost)
        );
    }


    // 글 공동 저자 설정
    @PatchMapping("/coauthors/{postId}")
    @Operation(
            summary = "글 공동 저자 설정 API", description = "글의 공동 저자를 설정합니다."
    )
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> setPostCoauthor(
            @PathVariable Long postId,
            @RequestBody PostRequestDTO.UpdateCoauthorRequestDTO request
    ) {
        Post updatedPost = postCommandService.updateCoauthors(postId, request);
        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toUpdatePostResultDTO(updatedPost)
        );
    }


    // 글의 소속 팀 설정
    @PatchMapping("/team/{postId}")
    @Operation(
            summary = "글의 소속 팀 설정 API", description = "글의 소속 팀을 설정합니다."
    )
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> setPostTeam(
            @PathVariable Long postId,
            @RequestBody PostRequestDTO.SetTeamRequestDTO request
    ) {
        Post updatedPost = postCommandService.setPostTeam(postId, request.getTeamId());
        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toUpdatePostResultDTO(updatedPost)
        );
    }


    // 저자의 다이어리 페이징 조회
    // TODO: 멤버별 작성한 글 조회시 공동 저자로 등록된 멤버도 조회가능하도록 기능 수정 필요
    @GetMapping("/member/{memberId}/paging")
    @Operation(
            summary = "저자의 다이어리 페이징 조회 API", description = "저자의 다이어리를 페이징으로 조회하기 위해 'Path Variable'로 해당 팀의 'memberId'를 받습니다. **첫 페이지는 0부터 입니다.**"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO.MemberPostPreviewListDTO> findPostByMember(
            @PathVariable Long memberId,
            @RequestParam @Min(0) Integer page,
            @RequestParam @Min(1) @Max(5) Integer size
    ) {
        // 토큰 유효성 검사 (memberId)
        //jwtTokenProvider.isValidToken(memberId);
        //List<Post> memberPostList = postQueryService.getMemberPost(memberId);
        Page<Post> posts = postQueryService.getPostsByMember(memberId, page, size);
        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toMemberPostPreviewListDTO(posts)
        );
    }

    // 팀의 다이어리 페이징 조회
    @GetMapping("/team/{teamId}/paging")
    @Operation(
            summary = "팀의 다이어리 페이징 조회 API", description = "팀의 다이어리를 페이징으로 조회하기 위해 'Path Variable'로 해당 팀의 'teamId'를 받습니다. **첫 페이지는 0부터 입니다.**"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO.TeamPostPreviewListDTO> findPostByTeam(
            @PathVariable Long teamId,
            @RequestParam @Min(0) Integer page,
            @RequestParam @Min(1) @Max(5) Integer size
    ){
        Page<Post> posts = postQueryService.getPostsByTeam(teamId, page, size);

        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toTeamPostPreviewListDTO(posts)
        );
    }

    // 프로젝트별 저자의 다이어리 페이징 조회
    @GetMapping("/project/{projectId}/member/{memberId}/paging")
    @Operation(
            summary = "프로젝트별 저자의 다이어리 페이징 조회 API", description = "프로젝트별 저자의 다이어리를 페이징으로 조회하기 위해 'Path Variable'로 해당 프로젝트의 'projectId'와 저자의 'memberId'를 받습니다. **첫 페이지는 0부터 입니다.**"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO.MemberPostInProjectPreviewListDTO> findPostByMemberInProject(
            @PathVariable Long projectId,
            @PathVariable Long memberId,
            @RequestParam @Min(0) Integer page,
            @RequestParam @Min(1) @Max(5) Integer size
    ){
        Page<Post> posts = postQueryService.getPostsByMemberInProject(projectId, memberId, page, size);

        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toMemberPostInProjectPreviewListDTO(posts)
        );
    }

    // 프로젝트별 팀의 다이어리 페이징 조회
    @GetMapping("/project/{projectId}/team/{teamId}/paging")
    @Operation(
            summary = "프로젝트별 팀의 다이어리 페이징 조회 API", description = "프로젝트별 팀의 다이어리를 페이징으로 조회하기 위해 'Path Variable'로 해당 프로젝트의 'projectId'와 팀의 'teamId'를 받습니다. **첫 페이지는 0부터 입니다.**"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO.TeamPostInProjectPreviewListDTO> findPostByTeamInProject(
            @PathVariable Long projectId,
            @PathVariable Long teamId,
            @RequestParam @Min(0) Integer page,
            @RequestParam @Min(1) @Max(5) Integer size
    ){
        Page<Post> posts = postQueryService.getPostsByTeamInProject(projectId, teamId, page, size);

        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toTeamPostInProjectPreviewListDTO(posts)
        );
    }

    // 팀별 저자의 다이어리 페이징 조회
    @GetMapping("/team/{teamId}/member/{memberId}/paging")
    @Operation(
            summary = "팀별 저자의 다이어리 페이징 조회 API", description = "팀별 저자의 다이어리를 페이징으로 조회하기 위해 'Path Variable'로 해당 팀의 'teamId'와 저자의 'memberId'를 받습니다. **첫 페이지는 0부터 입니다.**"
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO.MemberPostInTeamPreviewListDTO> findPostByMemberInTeam(
            @PathVariable Long teamId,
            @PathVariable Long memberId,
            @RequestParam @Min(0) Integer page,
            @RequestParam @Min(1) @Max(5) Integer size
    ){
        Page<Post> posts = postQueryService.getPostsByMemberInTeam(teamId, memberId, page, size);

        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toMemberPostInTeamPreviewListDTO(posts)
        );
    }

    // 제목으로 다이어리 페이징 조회
    @GetMapping("/title/paging")
    @Operation(
            summary = "제목으로 다이어리 페이징 조회 API", description = "제목으로 다이어리를 페이징으로 조회합니다. Param으로 제목을 입력하세요."
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO.PostPreviewListDTO> findPostsByTitle(
            @RequestParam Optional<String> search,
            @RequestParam @Min(0) Integer page,
            @RequestParam @Min(1) @Max(5) Integer size
    ) {
        Page<Post> posts = postQueryService.getPostsByTitle(Optional.of(search.orElse("")), page, size);
        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toPostPreviewListDTO(posts)
        );
    }

    // 카테고리명으로 다이어리 페이징 조회
    @GetMapping("/category/paging")
    @Operation(
            summary = "카테고리명으로 다이어리 페이징 조회 API", description = "카테고리명으로 다이어리 페이징 조회합니다."
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO> findPostByCategory(
    ){
        return null;
    }

    // 인접한 다이어리 조회 (특정 다이어리의 이전, 다음 다이어리 조회)
    @GetMapping("/{postId}/adjacent")
    @Operation(
            summary = "인접한 다이어리 조회 API", description = "특정 다이어리의 인접한 다이어리를 조회합니다."
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO.PostAdjacentDTO> findAdjacentPosts(
            @PathVariable Long postId
    ){
        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toPostAdjacentDTO(
                        postQueryService.findAdjacentPosts(postId)
                )
        );
    }



    // 글의 카테고리 설정
    @PatchMapping("/categories/{postId}")
    @Operation(
            summary = "글의 카테고리 설정 API", description = "글의 카테고리를 설정합니다."
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> setDiaryCategory(
            @PathVariable Long postId,
            @RequestBody Set<String> categoryNames // 카테고리 이름을 Set<String>으로 받습니다.
    ){
        // 게시글의 카테고리 설정
        Post updatedPost = postCommandService.setPostCategories(postId, categoryNames);

        // 결과를 DTO로 변환하여 응답
        return ApiResponse.onSuccess(
                SuccessStatus.POST_OK,
                PostConverter.toUpdatePostResultDTO(updatedPost)
        );
    }


    // 글 커스터마이징 옵션 변경
    @PatchMapping("/customize/{postId}")
    @Operation(
            summary = "글 커스터마이징 옵션 변경 API", description = "글의 커스터마이징 옵션을 변경합니다."
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO> customizeDiary(
    ){
        return null;
    }


    // AI로 코드 실행 미리보기
    @PostMapping("/code-preview/{postId}")
    @Operation(
            summary = "AI로 코드 실행 미리보기 API", description = "AI로 특정 글에 포함된 코드를 실행하여 미리보기 결과를 반환합니다."
            //, security = @SecurityRequirement(name = "accessToken")
    )
    public ApiResponse<PostResponseDTO> showDiaryCodePreview(
    ){
        return null;
    }

}
