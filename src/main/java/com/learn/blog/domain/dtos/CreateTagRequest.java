package com.learn.blog.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTagRequest {
    @NotBlank(message = "should contain at least one tag")
    @Size(max = 10, message = "Can't add more than 10 tags at once")
    private Set<
            @Size(min = 2, max = 30, message = "name should be between {min} and {max} characters")
            @Pattern(regexp = "^[\\w\\s-]+$", message = "should only contain letters, numbers, spaces and hyphenes")
            String> names;
}
