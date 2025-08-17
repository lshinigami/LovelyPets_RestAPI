package com.zhansaya.lovelypets.controller;

import com.zhansaya.lovelypets.dto.seller.SellerResponse;
import com.zhansaya.lovelypets.services.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerModerationController {
    private final SellerService service;

//  @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{sellerId}/verify")
    public SellerResponse verify(@PathVariable Long sellerId) {
        return service.verify(sellerId);
    }

//  @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{sellerId}/violation")
    public void addViolation(@PathVariable Long sellerId) {
        service.addViolationOrDelete(sellerId);
    }
}

