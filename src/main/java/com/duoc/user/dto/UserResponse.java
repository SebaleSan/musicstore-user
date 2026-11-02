package com.duoc.user.dto;

import com.duoc.user.model.Role;
import com.duoc.user.model.User;



public record UserResponse(Long id, String username, String email, Role role) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}
