package com.codiary.backend.global.web.dto.Post;

import com.codiary.backend.global.domain.enums.PostAccess;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostRequestDTO {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreatePostRequestDTO {
        private Long teamId;
        private Long projectId;
        private String postTitle;
        private String postBody;
        private Boolean postStatus;
        private String postCategory;
        private PostAccess postAccess;
        private List<MultipartFile> postFiles;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdatePostDTO {
        private String postTitle;
        private String postBody;
        private Boolean postStatus;
        private String postCategory;
        private PostAccess postAccess;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateVisibilityRequestDTO {
        private Boolean postStatus;  // 공개(true) / 비공개(false) 상태
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateCoauthorRequestDTO {
        private List<Long> memberIds;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetTeamRequestDTO {
        private Long teamId;
    }

}
