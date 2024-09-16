package com.codiary.backend.global.converter;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.PostFile;
import com.codiary.backend.global.web.dto.PostFile.PostFileResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class PostFileConverter {

    public static PostFile toPostFile(String fileUrl, Post post, String fileName) {
        return PostFile.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .post(post)
                .build();
    }

    public static PostFileResponseDTO.PostFileListDTO toPostFileListDTO(List<PostFile> postFileList) {
        PostFileResponseDTO.PostFileListDTO postFileListDTO = new PostFileResponseDTO.PostFileListDTO(postFileList);

        return postFileListDTO;
    }

    public static PostFileResponseDTO.PostFileDTO toPostFileDTO(PostFile postFile) {
        PostFileResponseDTO.PostFileDTO postFileDTO = new PostFileResponseDTO.PostFileDTO(postFile.getFileName(), postFile.getFileUrl());

        return postFileDTO;
    }
}
