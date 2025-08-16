package com.zhansaya.lovelypets.dto.user;

import com.zhansaya.lovelypets.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank @Size(max = 50) String firstname,
        @NotBlank @Size(max = 50) String lastname,
        @NotBlank @Email @Size(max = 50) String email,
        @NotBlank @Size(max = 20) String phone,
        @NotBlank @Size(min = 6, max = 100) String password,
        @NotNull Role role
) {}
