package com.zhansaya.lovelypets.services;

import com.zhansaya.lovelypets.domain.enums.OrderStatus;
import com.zhansaya.lovelypets.domain.models.Customer;
import com.zhansaya.lovelypets.domain.models.Order;
import com.zhansaya.lovelypets.dto.order.CreateOrderRequest;
import com.zhansaya.lovelypets.dto.order.OrderResponse;
import com.zhansaya.lovelypets.repositories.CustomerRepository;
import com.zhansaya.lovelypets.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepo;
    private final CustomerRepository customerRepo;

    public OrderResponse create(CreateOrderRequest req) {
        Customer customer = customerRepo.findById(req.customerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Order order = Order.builder()
                .customer(customer)
                .orderStatus(OrderStatus.PLACED)
                .build();

        return OrderResponse.from(orderRepo.save(order));
    }

    public OrderResponse ship(Long id) {
        Order order = findOrder(id);
        if (order.getOrderStatus() != OrderStatus.PLACED) {
            throw new IllegalStateException("Only PLACED orders can be shipped");
        }
        order.setOrderStatus(OrderStatus.ASSEMBLING);
        return OrderResponse.from(orderRepo.save(order));
    }

    public OrderResponse deliver(Long id) {
        Order order = findOrder(id);
        if (order.getOrderStatus() != OrderStatus.ASSEMBLING) {
            throw new IllegalStateException("Only ASSEMBLING orders can be delivered");
        }
        order.setOrderStatus(OrderStatus.IN_TRANSIT);
        return OrderResponse.from(orderRepo.save(order));
    }

    public OrderResponse cancel(Long id) {
        Order order = findOrder(id);
        if (order.getOrderStatus() == OrderStatus.READY) {
            throw new IllegalStateException("Cannot cancel a completed order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return OrderResponse.from(orderRepo.save(order));
    }

    @Transactional(readOnly = true)
    public OrderResponse get(Long id) {
        return OrderResponse.from(findOrder(id));
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> list(Pageable pageable, Long customerId, OrderStatus status) {
        if (customerId != null && status != null) {
            return orderRepo.findByCustomerIdAndOrderStatus(customerId, status, pageable)
                    .map(OrderResponse::from);
        } else if (customerId != null) {
            return orderRepo.findByCustomerId(customerId, pageable)
                    .map(OrderResponse::from);
        } else if (status != null) {
            return orderRepo.findByOrderStatus(status, pageable)
                    .map(OrderResponse::from);
        } else {
            return orderRepo.findAll(pageable).map(OrderResponse::from);
        }
    }

    private Order findOrder(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
}
