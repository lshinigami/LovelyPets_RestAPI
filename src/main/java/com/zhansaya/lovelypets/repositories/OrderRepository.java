package com.zhansaya.lovelypets.repositories;

import com.zhansaya.lovelypets.domain.models.Order;
import com.zhansaya.lovelypets.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);
    Page<Order> findByOrderStatus(OrderStatus status, Pageable pageable);
    Page<Order> findByCustomerIdAndOrderStatus(Long customerId, OrderStatus status, Pageable pageable);
}
