package com.zhansaya.lovelypets.repositories;

import com.zhansaya.lovelypets.domain.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
