package com.codiary.backend.domain.post.dto.request;

import com.codiary.backend.domain.post.enumerate.PostAccess;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostRequestDTO {


//    @Builder
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//    public record CreatePostRequestDTO (
//            Long teamId,
//            Long projectId,
//            String postTitle,
//            String postBody,
//            Boolean postStatus,
//            PostAccess postAccess,
//            String thumbnailImageName,
//            List<MultipartFile> postFiles
//    ) {
//    }

    @Getter
    //@Setter
    @Builder
    @AllArgsConstructor
    //@NoArgsConstructor
    public static class CreatePostRequestDTO {
        private Long teamId;
        private Long projectId;
        private String postTitle;
        private String postBody;
        private Boolean postStatus;
        //private Set<String> postCategory;
        private PostAccess postAccess;
        private String thumbnailImageName;
        private List<MultipartFile> postFiles;
    }

    @Getter
    //@Setter
    @Builder
    @AllArgsConstructor
    //@NoArgsConstructor
    public static class UpdatePostDTO {
        private String postTitle;
        private String postBody;
        private Boolean postStatus;
        //private Set<String> postCategory;
        private PostAccess postAccess;
        private String thumbnailImageName;
        private List<MultipartFile> addedPostFiles;
    }



}
