package com.learn.blog.mappers;

import com.learn.blog.domain.dtos.RegisterRequest;
import com.learn.blog.domain.dtos.RegisterResponse;
import com.learn.blog.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    RegisterRequest toRegisterRequest(User user);
    User toEntity(RegisterRequest registerRequest);
    RegisterResponse toRegisterResponse(User user);
}
