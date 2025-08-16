package com.zhansaya.lovelypets.repositories;

import com.zhansaya.lovelypets.domain.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    List<Seller> findByUserId(Long userId);
    boolean existsByUserIdAndOrganizationNameIgnoreCase(Long userId, String organizationName);
}
