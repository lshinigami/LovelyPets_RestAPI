package com.zhansaya.lovelypets;

import com.zhansaya.lovelypets.domain.enums.OrderStatus;
import com.zhansaya.lovelypets.domain.models.Customer;
import com.zhansaya.lovelypets.domain.models.Order;
import com.zhansaya.lovelypets.dto.order.CreateOrderRequest;
import com.zhansaya.lovelypets.dto.order.OrderResponse;
import com.zhansaya.lovelypets.repositories.CustomerRepository;
import com.zhansaya.lovelypets.repositories.OrderRepository;
import com.zhansaya.lovelypets.services.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepo;
    @Mock
    CustomerRepository customerRepo;

    @InjectMocks
    OrderService service;

    private static Customer customer(long id) {
        return Customer.builder().id(id).build();
    }

    private static Order order(long id, Customer c, OrderStatus st) {
        return Order.builder().id(id).customer(c).orderStatus(st).build();
    }

    @Test
    void create_ok() {
        var req = new CreateOrderRequest(42L);
        var c = customer(42L);

        when(customerRepo.findById(42L)).thenReturn(Optional.of(c));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(100L);
            return o;
        });

        OrderResponse resp = service.create(req);

        assertEquals(100L, resp.id());
        assertEquals(42L, resp.customerId());
        assertEquals(OrderStatus.PLACED, resp.orderStatus());
        verify(orderRepo).save(argThat(o ->
                o.getCustomer().getId().equals(42L) &&
                        o.getOrderStatus() == OrderStatus.PLACED
        ));
    }

    @Test
    void create_throws_whenCustomerNotFound() {
        var req = new CreateOrderRequest(99L);
        when(customerRepo.findById(99L)).thenReturn(Optional.empty());

        var ex = assertThrows(IllegalArgumentException.class, () -> service.create(req));
        assertTrue(ex.getMessage().toLowerCase().contains("customer not found"));
        verify(orderRepo, never()).save(any());
    }

    @Test
    void ship_fromPlaced_ok() {
        var c = customer(1L);
        var o = order(7L, c, OrderStatus.PLACED);
        when(orderRepo.findById(7L)).thenReturn(Optional.of(o));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderResponse resp = service.ship(7L);

        assertEquals(OrderStatus.ASSEMBLING, resp.orderStatus());
        verify(orderRepo).save(argThat(ord -> ord.getOrderStatus() == OrderStatus.ASSEMBLING));
    }

    @Test
    void ship_wrongState_throws() {
        var c = customer(1L);
        var o = order(7L, c, OrderStatus.ASSEMBLING);
        when(orderRepo.findById(7L)).thenReturn(Optional.of(o));

        var ex = assertThrows(IllegalStateException.class, () -> service.ship(7L));
        assertTrue(ex.getMessage().toLowerCase().contains("only placed"));
        verify(orderRepo, never()).save(any());
    }

    @Test
    void deliver_fromAssembling_ok() {
        var c = customer(2L);
        var o = order(8L, c, OrderStatus.ASSEMBLING);
        when(orderRepo.findById(8L)).thenReturn(Optional.of(o));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderResponse resp = service.deliver(8L);

        assertEquals(OrderStatus.IN_TRANSIT, resp.orderStatus());
        verify(orderRepo).save(argThat(ord -> ord.getOrderStatus() == OrderStatus.IN_TRANSIT));
    }
    @Test
    void cancel_fromNotReady_ok() {
        var c = customer(3L);
        var o = order(9L, c, OrderStatus.IN_TRANSIT);
        when(orderRepo.findById(9L)).thenReturn(Optional.of(o));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderResponse resp = service.cancel(9L);

        assertEquals(OrderStatus.CANCELLED, resp.orderStatus());
        verify(orderRepo).save(argThat(ord -> ord.getOrderStatus() == OrderStatus.CANCELLED));
    }

    @Test
    void cancel_fromReady_throws() {
        var c = customer(4L);
        var o = order(10L, c, OrderStatus.READY);
        when(orderRepo.findById(10L)).thenReturn(Optional.of(o));

        var ex = assertThrows(IllegalStateException.class, () -> service.cancel(10L));
        assertTrue(ex.getMessage().toLowerCase().contains("cannot cancel a completed order"));
        verify(orderRepo, never()).save(any());
    }
}
