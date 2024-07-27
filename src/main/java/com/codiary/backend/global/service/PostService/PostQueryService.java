package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostQueryService {

    List<Post> findAllBySearch(Optional<String> optSearch);
    List<Post> getMemberPost(Long memberId);
    Page<Post> getPostsByTeam(Long teamId, int page, int size);
    Page<Post> getPostsByMemberInProject(Long projectId, Long memberId, int page, int size);


    Map<String, List<String>> getPostsByMonth(Long memberId, YearMonth yearMonth);
}
