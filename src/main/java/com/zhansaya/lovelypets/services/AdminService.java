package com.zhansaya.lovelypets.services;

import com.zhansaya.lovelypets.domain.models.Admin;
import com.zhansaya.lovelypets.domain.models.User;
import com.zhansaya.lovelypets.domain.enums.Role;
import com.zhansaya.lovelypets.dto.admin.AdminResponse;
import com.zhansaya.lovelypets.dto.admin.CreateAdminRequest;
import com.zhansaya.lovelypets.repositories.AdminRepository;
import com.zhansaya.lovelypets.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public AdminResponse addAdmin(CreateAdminRequest req) {
        var user = userRepository.findById(req.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setRole(Role.ADMIN);
        userRepository.save(user);

        var admin = Admin.builder()
                .user(user)
                .isActive(true)
                .responsibleFor(req.responsibleFor())
                .build();

        return AdminResponse.from(adminRepository.save(admin));
    }

    public void deactivate(Long adminId) {
        var admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        admin.setIsActive(false);
        adminRepository.save(admin);
    }

    public AdminResponse changeResponsibilities(Long adminId, String newResponsibilities) {
        var admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        admin.setResponsibleFor(newResponsibilities);
        return AdminResponse.from(adminRepository.save(admin));
    }

    public List<Admin> getActiveAdmins() {
        return adminRepository.findByIsActiveTrue();
    }

    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }
}

