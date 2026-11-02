package com.duoc.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.duoc.user.model.Role;

public record UserUpdateRequest(
    @NotBlank @Email @Size (max = 100)String email,
    @Size (min = 8, max = 100)String password,
    @NotNull Role role
) {
    
}
