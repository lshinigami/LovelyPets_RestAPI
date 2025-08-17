package com.zhansaya.lovelypets.controller;

import com.zhansaya.lovelypets.dto.customer.CustomerResponse;
import com.zhansaya.lovelypets.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerAdminController {
    private final CustomerService service;

//  @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{customerId}/discount")
    public CustomerResponse setDiscount(@PathVariable Long customerId, @RequestParam int percent) {
        return service.setDiscount(customerId, percent);
    }
}
