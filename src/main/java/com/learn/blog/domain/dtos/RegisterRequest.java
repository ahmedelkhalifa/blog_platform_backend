package com.learn.blog.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "username can't be empty")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "Can only contain letter, numbers, spaces and hyphens")
    private String name;
    @NotBlank(message = "password can't be empty")
    private String password;
    @NotBlank(message = "email can't be empty")
    private String email;
}
