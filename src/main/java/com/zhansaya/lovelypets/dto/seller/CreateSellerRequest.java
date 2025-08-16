package com.zhansaya.lovelypets.dto.seller;

import com.zhansaya.lovelypets.domain.enums.OrganizationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateSellerRequest(
        @NotNull Long userId,
        @NotBlank @Size(max = 50) String organizationName,
        @NotNull OrganizationType organizationType,
        @Size(max = 12) String iin,
        @Size(max = 12) String bin
) {}
