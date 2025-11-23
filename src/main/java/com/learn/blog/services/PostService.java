package com.learn.blog.services;

import com.learn.blog.domain.PostStatus;
import com.learn.blog.domain.dtos.CreatePostRequest;
import com.learn.blog.domain.entities.Category;
import com.learn.blog.domain.entities.Post;
import com.learn.blog.domain.entities.Tag;
import com.learn.blog.domain.entities.User;
import com.learn.blog.repositories.CategoryRepository;
import com.learn.blog.repositories.PostRepository;
import com.learn.blog.security.BlogUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    private final int WORD_PER_MINUTE = 200;

    @Transactional()
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null || tagId != null) {
            Category category = categoryService.getById(categoryId);
            Tag tag = tagService.getById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(PostStatus.PUBLISHED, category, tag);
        }
        if (categoryId != null) {
            Category category = categoryService.getById(categoryId);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
        }
        if (tagId != null) {
            Tag tag = tagService.getById(tagId);
            return postRepository.findAllByStatusAndTags(PostStatus.PUBLISHED, tag);
        }
        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    public List<Post> getAllDrafts() {
        BlogUserDetails userDetails = (BlogUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        User user = User.builder()
                .id(userDetails.getId())
                .email(userDetails.getUsername())
                .password(userDetails.getPassword())
                .build();
        return postRepository.findAllByStatusAndAuthor(PostStatus.DRAFT, user);
    }

    public Post createPost(CreatePostRequest createPostRequest) {
        BlogUserDetails userDetails = (BlogUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        User user = User.builder()
                .id(userDetails.getId())
                .email(userDetails.getUsername())
                .password(userDetails.getPassword())
                .build();
        Category category = categoryService.getById(createPostRequest.getCategoryId());
        List<Tag> tags = tagService.getByIds(createPostRequest.getTagsId());
        Post post = Post.builder()
                .title(createPostRequest.getTitle())
                .author(user)
                .category(category)
                .tags(new HashSet<>(tags))
                .content(createPostRequest.getContent())
                .status(createPostRequest.getStatus())
                .readingTime(calculateReadingTime(createPostRequest.getContent()))
                .build();
        return postRepository.save(post);
    }

    private Integer calculateReadingTime(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }
        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / WORD_PER_MINUTE);
    }
}
