package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Bookmark;
import com.codiary.backend.global.domain.entity.Follow;
import com.codiary.backend.global.domain.entity.Member;
import com.codiary.backend.global.web.dto.Bookmark.BookmarkResponseDTO;
import com.codiary.backend.global.web.dto.Member.FollowResponseDto;
import com.codiary.backend.global.web.dto.Member.MemberResponseDTO;
import com.codiary.backend.global.web.dto.Member.MemberSumResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberConverter {

    public FollowResponseDto toManageFollowDto(Follow follow) {
        return FollowResponseDto.builder()
                .followId(follow.getFollowId())
                .followerId(follow.getFromMember().getMemberId())
                .followerName(follow.getFromMember().getNickname())
                .followingId(follow.getToMember().getMemberId())
                .followingName(follow.getToMember().getNickname())
                .followStatus(follow.getFollowStatus())
                .build();
    }

    public MemberSumResponseDto toFollowResponseDto(Member member) {
        return MemberSumResponseDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .photoUrl(member.getPhotoUrl())
                .build();
    }



    // 회원별 북마크 리스트 조회
    public static MemberResponseDTO.BookmarkDTO toBookmarkDTO(Bookmark bookmark) {
        return MemberResponseDTO.BookmarkDTO.builder()
                .memberId(bookmark.getMember().getMemberId())
                .bookmarkId(bookmark.getId())
                .postId(bookmark.getPost().getPostId())
                // Post의 첫 번째 사진을 가져온다
                // 나중에 postFile이 존재하지 않을 경우 나오는 기본 사진 처리를 해줘야 한다
                .photoUrl(bookmark.getPost().getPostFileList().get(0).getFileUrl())
                .postTitle(bookmark.getPost().getPostTitle())
                .nickname(bookmark.getPost().getMember().getNickname())
                .postBody(bookmark.getPost().getPostBody())
                .createdAt(bookmark.getPost().getCreatedAt())
                .build();



//        final String DEFAULT_PHOTO_URL = "default_photo_url"; // 기본값 설정
//        // Optional을 사용하여 photoUrl을 안전하게 추출
//        String photoUrl = Optional.ofNullable(bookmark.getPost().getPostFileList())
//                .filter(list -> !list.isEmpty())
//                .map(list -> list.get(0).getFileUrl())
//                .orElse("default_photo_url"); // 기본값을 설정합니다.
    }

    public static MemberResponseDTO.BookmarkListDTO toBookmarkListDTO(Page<Bookmark> bookmarkList) {
        List<MemberResponseDTO.BookmarkDTO> bookmarkDTOList = bookmarkList.stream()
                .map(MemberConverter::toBookmarkDTO).collect(Collectors.toList());

        return MemberResponseDTO.BookmarkListDTO.builder()
                .isLast(bookmarkList.isLast())
                .isFirst(bookmarkList.isFirst())
                .totalPage(bookmarkList.getTotalPages())
                .totalElements(bookmarkList.getTotalElements())
                .listSize(bookmarkDTOList.size())
                .bookmarkList(bookmarkDTOList)
                .build();
    }

}
