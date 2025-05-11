package com.example.login_api.mapper;

import com.example.login_api.dto.UserResponse;
import com.example.login_api.entity.UserEntity;

public class UserMapper {

    public static UserResponse toDto(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    // Si necesitas mapear de DTO a entidad también:
    public static UserEntity toEntity(UserResponse dto) {
        UserEntity user = new UserEntity();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        return user;
    }
}