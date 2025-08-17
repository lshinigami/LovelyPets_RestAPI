package com.zhansaya.lovelypets.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAdminRequest(
        @NotNull Long userId,
        @NotBlank String responsibleFor,
        @NotNull Boolean isActive
) {}
