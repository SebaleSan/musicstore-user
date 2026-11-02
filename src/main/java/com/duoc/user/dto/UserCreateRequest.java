package com.duoc.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.duoc.user.model.Role;

public record UserCreateRequest(
    @NotBlank @Size (min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El nombre de usuario solo puede contener letras, números y guiones bajos")String username,
    @NotBlank @Email @Size (max = 100)String email,
    @NotBlank @Size (min = 8, max = 100)String password,
    @NotNull Role role
    
){}