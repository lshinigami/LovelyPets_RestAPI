package com.zhansaya.lovelypets.dto.order;


import com.zhansaya.lovelypets.domain.enums.OrderStatus;
import com.zhansaya.lovelypets.domain.models.Order;

import java.time.Instant;

public record OrderResponse(
        Long id,
        Long customerId,
        Instant orderDate,
        OrderStatus orderStatus
) {
    public static OrderResponse from(Order o) {
        return new OrderResponse(
                o.getId(),
                o.getCustomer().getId(),
                o.getOrderDate(),
                o.getOrderStatus()
        );
    }
}

