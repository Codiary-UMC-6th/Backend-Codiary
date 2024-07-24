package com.codiary.backend.global.web.dto.Post;

import com.codiary.backend.global.domain.enums.PostAccess;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostRequestDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreatePostRequestDTO {
        private Long teamId;
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
    public static class UpdatePostDTO {
        private String postTitle;
        private String postBody;
        private Boolean postStatus;
        private String postCategory;
        private PostAccess postAccess;
    }




}
