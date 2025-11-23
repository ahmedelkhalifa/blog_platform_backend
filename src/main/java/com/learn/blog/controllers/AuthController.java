package com.learn.blog.controllers;

import com.learn.blog.domain.dtos.AuthResponse;
import com.learn.blog.domain.dtos.LoginRequest;
import com.learn.blog.domain.dtos.RegisterRequest;
import com.learn.blog.domain.dtos.RegisterResponse;
import com.learn.blog.domain.entities.User;
import com.learn.blog.mappers.UserMapper;
import com.learn.blog.repositories.UserRepository;
import com.learn.blog.services.AuthService;
import com.learn.blog.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        UserDetails userDetails = authService.authenticate(loginRequest.getEmail(),
                loginRequest.getPassword());
        String tokenValue = jwtService.generateToken(userDetails);
        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(registerRequest.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            newUser.setName(registerRequest.getName());
            return newUser;
        });
        userRepository.save(user);
        RegisterResponse registerResponse = userMapper.toRegisterResponse(user);
        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }
}
