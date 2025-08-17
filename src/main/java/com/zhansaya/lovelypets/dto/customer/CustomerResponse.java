package com.zhansaya.lovelypets.dto.customer;

import com.zhansaya.lovelypets.domain.models.Customer;
import com.zhansaya.lovelypets.domain.models.User;

public record CustomerResponse(
        Long id,
        Long userId,
        String firstname,
        String lastname,
        String email,
        String city,
        String address,
        Boolean loyaltyMember,
        Integer discountPercent
) {
    public static CustomerResponse from(Customer c) {
        User u = c.getUser();
        return new CustomerResponse(
                c.getId(),
                u.getId(),
                u.getFirstname(),
                u.getLastname(),
                u.getEmail(),
                c.getCity(),
                c.getAddress(),
                c.getLoyaltyMember(),
                c.getDiscountPercent()
        );
    }
}
