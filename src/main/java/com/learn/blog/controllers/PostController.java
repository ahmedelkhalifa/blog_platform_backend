package com.learn.blog.controllers;

import com.learn.blog.domain.dtos.CreatePostRequest;
import com.learn.blog.domain.dtos.PostDto;
import com.learn.blog.domain.entities.Post;
import com.learn.blog.mappers.PostMapper;
import com.learn.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    @GetMapping
    public ResponseEntity<List<PostDto>> listPosts(@RequestParam(required = false) UUID categoryId,
                                                   @RequestParam(required = false) UUID tagId) {
        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/drafts")
    public ResponseEntity<List<PostDto>> getAllDrafts() {
        List<PostDto> postDtos = postService.getAllDrafts().stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostRequest createPostRequest) {
        Post post = postService.createPost(createPostRequest);
        PostDto postDto = postMapper.toDto(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postDto);
    }
}
