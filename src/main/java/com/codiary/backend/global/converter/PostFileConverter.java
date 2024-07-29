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
        PostFileResponseDTO.PostFileListDTO postFileListDTO = new PostFileResponseDTO.PostFileListDTO();
        postFileListDTO.setPostFileList(new ArrayList<>());

        for (PostFile postFile:postFileList) {
            postFileListDTO.getPostFileList().add(toPostFileDTO(postFile));
        }

        return postFileListDTO;
    }

    public static PostFileResponseDTO.postFileDTO toPostFileDTO(PostFile postFile) {
        PostFileResponseDTO.postFileDTO postFileDTO = new PostFileResponseDTO.postFileDTO();

        postFileDTO.setName(postFileDTO.getName());
        postFileDTO.setUrl(postFileDTO.getUrl());

        return postFileDTO;
    }
}
