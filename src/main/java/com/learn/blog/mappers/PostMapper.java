package com.learn.blog.mappers;

import com.learn.blog.domain.PostStatus;
import com.learn.blog.domain.dtos.CategoryDto;
import com.learn.blog.domain.dtos.CreateCategoryRequest;
import com.learn.blog.domain.dtos.PostDto;
import com.learn.blog.domain.dtos.TagResponse;
import com.learn.blog.domain.entities.Category;
import com.learn.blog.domain.entities.Post;
import com.learn.blog.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category", qualifiedByName = "getCategoryDto")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "getTagResponses")
    @Mapping(target = "status", source = "status")
    PostDto toDto(Post post);

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostsCount")
    CategoryDto toDto(Category category);

    @Named("calculatePostsCount")
    default long calculatePostsCount(List<Post> posts) {
        if (posts == null) {return 0;}
        return posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus())).count();
    }

    @Named("getCategoryDto")
    default CategoryDto getCategoryDto(Category category) {
        return toDto(category);
    }

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostsCount")
    TagResponse toTagResponse(Tag tag);

    @Named("calculatePostsCount")
    default Integer calculatePostsCount(Set<Post> posts) {
        if (posts == null) {return 0;}
        return (int) posts.stream().filter(post ->
                PostStatus.PUBLISHED.equals(post.getStatus())).count();
    }

    @Named("getTagResponses")
    default Set<TagResponse> getTagResponses(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> toTagResponse(tag)).collect(Collectors.toSet());
    }

}
