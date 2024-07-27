package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.PostFile;

public class PostFileConverter {

    public static PostFile toPostFile(String fileUrl, Post post) {
        return PostFile.builder()
                .fileUrl(fileUrl)
                .post(post)
                .build();
    }
}
