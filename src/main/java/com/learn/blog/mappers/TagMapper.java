package com.learn.blog.mappers;

import com.learn.blog.domain.PostStatus;
import com.learn.blog.domain.dtos.TagResponse;
import com.learn.blog.domain.entities.Post;
import com.learn.blog.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "postsCount", source = "posts", qualifiedByName = "calculatePostsCount")
    TagResponse toTagResponse(Tag tag);

    @Named("calculatePostsCount")
    default Integer calculatePostsCount(Set<Post> posts) {
        if (posts == null) {return 0;}
        return (int) posts.stream().filter(post ->
                PostStatus.PUBLISHED.equals(post.getStatus())).count();
    }
}
