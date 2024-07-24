package com.codiary.backend.global.service.PostService;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.repository.PostRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@Getter
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;

    @Override
    public List<Post> findAllBySearch(Optional<String> optSearch) {
        // 만약 검색어가 존재한다면
        if (optSearch.isPresent()) {
            String search = optSearch.get();

            return postRepository.findAllByPostTitleContainingIgnoreCaseOrderByCreatedAtDesc(search);
        }
        // 검색어 존재 X
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
}
