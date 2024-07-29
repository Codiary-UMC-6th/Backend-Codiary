package com.codiary.backend.global.web.dto.PostFile;

import com.codiary.backend.global.domain.entity.PostFile;
import lombok.*;

import java.util.List;

public class PostFileResponseDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostFileListDTO {
        List<postFileDTO> postFileList;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class postFileDTO {
        String name;
        String url;
    }
}
