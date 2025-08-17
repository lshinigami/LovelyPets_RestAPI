package com.zhansaya.lovelypets.controller;

import com.zhansaya.lovelypets.dto.admin.AdminResponse;
import com.zhansaya.lovelypets.dto.admin.ChangeResponsibilitiesRequest;
import com.zhansaya.lovelypets.dto.admin.CreateAdminRequest;
import com.zhansaya.lovelypets.services.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;

//  @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public AdminResponse addAdmin(@RequestBody @Valid CreateAdminRequest req) {
        return service.addAdmin(req);
    }

//  @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{adminId}/deactivate")
    public void deactivate(@PathVariable Long adminId) {
        service.deactivate(adminId);
    }

//  @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{adminId}/responsibilities")
    public AdminResponse changeResponsibilities(@PathVariable Long adminId, @RequestBody ChangeResponsibilitiesRequest req) {
        return service.changeResponsibilities(adminId, req.responsibleFor());
    }
}

