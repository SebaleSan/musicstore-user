package com.duoc.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileUpdateRequest(
    @NotBlank @Email @Size (max = 100)String email,
    @Size (min = 8, max = 100)String password
){}
