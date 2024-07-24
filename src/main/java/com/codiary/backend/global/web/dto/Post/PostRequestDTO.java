package com.codiary.backend.global.web.dto.Post;

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
        private String postTitle;
        private String postBody;
        private Boolean postStatus;
        private String postCategory;
    }


}
