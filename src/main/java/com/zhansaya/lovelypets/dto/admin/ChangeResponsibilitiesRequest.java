package com.zhansaya.lovelypets.dto.admin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangeResponsibilitiesRequest(
        @NotNull Long adminId,
        @NotBlank String newResponsibilities,
        @NotBlank String responsibleFor
) {}
