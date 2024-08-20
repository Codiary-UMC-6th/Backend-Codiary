package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.domain.entity.Post;
import org.springframework.data.domain.Page;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface PostQueryService {

    Page<Post> getPostsByTitle(Optional<String> optSearch, int page, int size);
    Page<Post> getPostsByCategories(Optional<String> optSearch, int page, int size);
    Page<Post> getPostsByMember(Long memberId, int page, int size);
    Page<Post> getPostsByTeam(Long teamId, int page, int size);
    Page<Post> getPostsByMemberInProject(Long projectId, Long memberId, int page, int size);
    Page<Post> getPostsByTeamInProject(Long projectId, Long teamId, int page, int size);
    Page<Post> getPostsByMemberInTeam(Long teamId, Long memberId, int page, int size);
    Post.PostAdjacent findAdjacentPosts(Long postId);

    List<Post> getPostsByMonth(Member member, YearMonth yearMonth);

    // 메인페이지 인기글 전체 리스트 조회
    Page<Post> getPostPopularList(Integer page);
    // 메인페이지 인기글 멤버 관심 카테고리별 리스트 조회
    Page<Post> getPostPopularMemberCategoryList(Long memberCategoryId, Integer page);
    // 메인페이지 최신글 리스트 조회
    Page<Post> getPostLatestList(Integer page);
    // 메인페이지 팔로잉 게시글 리스트 조회
    Page<Post> getPostFollowingList(Long followId, Integer page);
    // 제목 & 본문 & 저자 & 프로젝트 & 카테고리 검색
    Page<Post> getPostSearchTitleList(Optional<String> keyword, Integer page);

    Post getPost(Long postId);
}
