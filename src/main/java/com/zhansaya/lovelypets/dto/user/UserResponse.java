package com.zhansaya.lovelypets.dto.user;

import com.zhansaya.lovelypets.domain.enums.Role;

public record UserResponse(
        Long id, String firstname, String lastname,
        String email, String phone, Role role
) {
    public static UserResponse from(com.zhansaya.lovelypets.domain.models.User u) {
        return new UserResponse(u.getId(), u.getFirstname(), u.getLastname(),
                u.getEmail(), u.getPhone(), u.getRole());
    }
}
