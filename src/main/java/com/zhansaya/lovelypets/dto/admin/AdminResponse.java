package com.zhansaya.lovelypets.dto.admin;

import com.zhansaya.lovelypets.domain.models.Admin;
import com.zhansaya.lovelypets.domain.models.User;

public record AdminResponse(
        Long id,
        Long userId,
        String firstname,
        String lastname,
        String email,
        Boolean isActive,
        String responsibleFor
) {
    public static AdminResponse from(Admin admin) {
        User u = admin.getUser();
        return new AdminResponse(
                admin.getId(),
                u.getId(),
                u.getFirstname(),
                u.getLastname(),
                u.getEmail(),
                admin.getIsActive(),
                admin.getResponsibleFor()
        );
    }
}
