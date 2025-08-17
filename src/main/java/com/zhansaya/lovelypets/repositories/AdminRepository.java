package com.zhansaya.lovelypets.repositories;

import com.zhansaya.lovelypets.domain.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findByIsActiveTrue();
}
