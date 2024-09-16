package com.codiary.backend.global.web.dto.PostFile;

import com.codiary.backend.global.domain.entity.PostFile;

import java.util.List;

public class PostFileResponseDTO {

    public record PostFileListDTO(List<PostFile> postFileList) {}

    public record PostFileDTO(String name, String url) {}
}
