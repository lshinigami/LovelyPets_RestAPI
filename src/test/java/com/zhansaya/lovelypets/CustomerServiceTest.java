package com.zhansaya.lovelypets;

import com.zhansaya.lovelypets.domain.models.Customer;
import com.zhansaya.lovelypets.domain.models.User;
import com.zhansaya.lovelypets.repositories.CustomerRepository;
import com.zhansaya.lovelypets.services.CustomerService;
import com.zhansaya.lovelypets.dto.customer.CustomerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        User user = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .email("john@example.com")
                .build();
        customer = Customer.builder()
                .id(1L)
                .user(user)
                .city("City")
                .address("Address")
                .loyaltyMember(false)
                .discountPercent(0)
                .build();
    }

    @Test
    void setDiscount_withValidPercent_setsCorrectly() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        CustomerResponse response = customerService.setDiscount(1L, 20);
        assertEquals(20, response.discountPercent());
        verify(customerRepository).save(customer);
    }

    @Test
    void setDiscount_withNegativePercent_setsZero() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        CustomerResponse response = customerService.setDiscount(1L, -10);
        assertEquals(0, response.discountPercent());
        verify(customerRepository).save(customer);
    }

    @Test
    void setDiscount_withPercentAbove100_sets100() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        CustomerResponse response = customerService.setDiscount(1L, 150);
        assertEquals(100, response.discountPercent());
        verify(customerRepository).save(customer);
    }

    @Test
    void setDiscount_customerNotFound_throwsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> customerService.setDiscount(1L, 20));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void setDiscount_responseContainsUserData() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        CustomerResponse response = customerService.setDiscount(1L, 30);
        assertEquals("John", response.firstname());
        assertEquals("Doe", response.lastname());
        assertEquals("john@example.com", response.email());
    }
}

