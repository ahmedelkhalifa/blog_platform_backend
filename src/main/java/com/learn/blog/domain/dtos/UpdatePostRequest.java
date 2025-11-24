package com.learn.blog.domain.dtos;

import com.learn.blog.domain.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePostRequest {
    @NotNull(message = "id can't be empty")
    private UUID id;
    @NotBlank(message = "title shouldn't be blank")
    @Size(min = 2, max = 200, message = "title should be between {min} and {max} characters")
    private String title;
    @NotBlank(message = "content shouldn't be blank")
    @Size(min = 2, max = 50000, message = "title should be between {min} and {max} characters")
    private String content;
    @NotNull(message = "category id is required")
    private UUID categoryId;
    @Builder.Default
    @Size(max = 10, message = "No more than {max} tags are allowed")
    private Set<UUID> tagIds = new HashSet<>();
    @NotNull(message = "status is required")
    private PostStatus status;
}
