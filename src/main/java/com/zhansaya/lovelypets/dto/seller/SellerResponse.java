package com.zhansaya.lovelypets.dto.seller;

import com.zhansaya.lovelypets.domain.enums.OrganizationType;

public record SellerResponse(
        Long id,
        Long userId,
        String organizationName,
        OrganizationType organizationType,
        String iin,
        String bin
) {
    public static SellerResponse from(com.zhansaya.lovelypets.domain.models.Seller s) {
        return new SellerResponse(
                s.getId(),
                s.getUser().getId(),
                s.getOrganizationName(),
                s.getOrganizationType(),
                s.getIin(),
                s.getBin()
        );
    }
}
