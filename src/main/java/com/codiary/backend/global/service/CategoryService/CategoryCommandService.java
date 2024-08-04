package com.codiary.backend.global.service.CategoryService;

import com.codiary.backend.global.domain.entity.Post;
import com.codiary.backend.global.domain.entity.mapping.Categories;
import com.codiary.backend.global.domain.entity.mapping.MemberCategory;

public interface CategoryCommandService {

    Categories addCategory(Post post, String categoryName);

}
