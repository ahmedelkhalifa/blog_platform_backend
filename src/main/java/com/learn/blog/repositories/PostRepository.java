package com.learn.blog.repositories;

import com.learn.blog.domain.PostStatus;
import com.learn.blog.domain.entities.Category;
import com.learn.blog.domain.entities.Post;
import com.learn.blog.domain.entities.Tag;
import com.learn.blog.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByStatusAndCategoryAndTagsContaining(PostStatus postStatus, Category category, Tag tag);
    List<Post> findAllByStatusAndCategory(PostStatus postStatus, Category category);
    List<Post> findAllByStatusAndTags(PostStatus postStatus, Tag tag);
    List<Post> findAllByStatus(PostStatus postStatus);
    List<Post> findAllByStatusAndAuthor(PostStatus postStatus, User user);
}
