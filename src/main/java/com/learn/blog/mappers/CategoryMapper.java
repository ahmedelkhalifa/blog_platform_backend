package com.learn.blog.mappers;

import com.learn.blog.domain.PostStatus;
import com.learn.blog.domain.dtos.CategoryDto;
import com.learn.blog.domain.dtos.CreateCategoryRequest;
import com.learn.blog.domain.entities.Category;
import com.learn.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postsCount", source = "posts", qualifiedByName = "calculatePostsCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);

    @Named("calculatePostsCount")
    default long calculatePostsCount(List<Post> posts) {
        if (posts == null) {return 0;}
        return posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus())).count();
    }
}
