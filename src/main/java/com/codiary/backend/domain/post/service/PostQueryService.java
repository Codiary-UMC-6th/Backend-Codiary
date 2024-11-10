package com.codiary.backend.domain.post.service;

import com.codiary.backend.domain.member.entity.Member;
import com.codiary.backend.domain.member.repository.MemberRepository;
import com.codiary.backend.domain.post.entity.Post;
import com.codiary.backend.domain.post.repository.PostRepository;
import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.domain.team.repository.TeamRepository;
import com.codiary.backend.global.apiPayload.code.status.ErrorStatus;
import com.codiary.backend.global.apiPayload.exception.handler.PostHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final TeamRepository teamRepository;

    public Page<Post> getPostsByMember(Long memberId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        List<Post> postsByMember = postRepository.findByMemberOrderByCreatedAtDescPostIdDesc(member, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        List<Post> postsByCoauthor = postRepository.findByAuthorsList_MemberOrderByCreatedAtDescPostIdDesc(member, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

        List<Post> combinedPosts = new ArrayList<>();
        combinedPosts.addAll(postsByMember);
        combinedPosts.addAll(postsByCoauthor);

        if (combinedPosts.isEmpty()) { throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_MEMBER); }
        combinedPosts.sort(Comparator.comparing(Post::getCreatedAt).reversed());

        int start = Math.min(page * size, combinedPosts.size());
        int end = Math.min((page + 1) * size, combinedPosts.size());
        return new PageImpl<>(combinedPosts.subList(start, end), request, combinedPosts.size());
    }

    public Page<Post> getPostsByTeam(Long teamId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Team team = teamRepository.findById(teamId).get();

        if (!postRepository.existsByTeam(team)){ throw new PostHandler(ErrorStatus.POST_NOT_EXIST_BY_TEAM); }
        return postRepository.findByTeamOrderByCreatedAtDescPostIdDesc(team, request);
    }

}
