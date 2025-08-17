package com.zhansaya.lovelypets.services;

import com.zhansaya.lovelypets.dto.customer.CustomerResponse;
import com.zhansaya.lovelypets.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerResponse setDiscount(Long customerId, int percent) {
        if (percent < 0) percent = 0;
        if (percent > 100) percent = 100;

        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        customer.setDiscountPercent(percent);
        customerRepository.save(customer);

        return CustomerResponse.from(customer);
    }
}

