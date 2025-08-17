package com.zhansaya.lovelypets.controller;

import com.zhansaya.lovelypets.dto.order.CreateOrderRequest;
import com.zhansaya.lovelypets.dto.order.OrderResponse;
import com.zhansaya.lovelypets.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

//  @PreAuthorize("hasRole('SELLER')")
    @PostMapping
    public OrderResponse create(@RequestBody CreateOrderRequest req) {
        return service.create(req);
    }

//  @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/{id}/ship")
    public OrderResponse ship(@PathVariable Long id) { return service.ship(id); }

//  @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/{id}/deliver")
    public OrderResponse deliver(@PathVariable Long id) { return service.deliver(id); }

//  @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable Long id) { return service.cancel(id); }

//  @PreAuthorize("hasAnyRole('ADMIN','SELLER','CUSTOMER')")
    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable Long id) {
        return service.get(id);
    }

//  @PreAuthorize("hasAnyRole('ADMIN','SELLER','CUSTOMER')")
    @GetMapping
    public Page<OrderResponse> list(
            Pageable pageable,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) com.zhansaya.lovelypets.domain.enums.OrderStatus status
    ) {
        return service.list(pageable, customerId, status);
    }
}
