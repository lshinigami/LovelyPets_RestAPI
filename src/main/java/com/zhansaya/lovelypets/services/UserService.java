package com.zhansaya.lovelypets.services;

import com.zhansaya.lovelypets.domain.models.User;
import com.zhansaya.lovelypets.dto.user.CreateUserRequest;
import com.zhansaya.lovelypets.dto.user.UpdateUserRequest;
import com.zhansaya.lovelypets.dto.user.UserResponse;
import com.zhansaya.lovelypets.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserResponse create(CreateUserRequest req) {
        if (repo.existsByEmail(req.email())) throw new DataIntegrityViolationException("email already used");
        if (repo.existsByPhone(req.phone())) throw new DataIntegrityViolationException("phone already used");

        User u = User.builder()
                .firstname(req.firstname())
                .lastname(req.lastname())
                .email(req.email())
                .phone(req.phone())
                .password(passwordEncoder.encode(req.password()))
                .role(req.role())
                .build();
        return UserResponse.from(repo.save(u));
    }

    @Transactional(readOnly = true)
    public UserResponse get(Long id) {
        return UserResponse.from(
                repo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"))
        );
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> list(Pageable pageable) {
        return repo.findAll(pageable).map(UserResponse::from);
    }

    public UserResponse update(Long id, UpdateUserRequest req) {
        User u = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!u.getEmail().equals(req.email()) && repo.existsByEmail(req.email()))
            throw new DataIntegrityViolationException("email already used");
        if (!u.getPhone().equals(req.phone()) && repo.existsByPhone(req.phone()))
            throw new DataIntegrityViolationException("phone already used");

        u.setFirstname(req.firstname());
        u.setLastname(req.lastname());
        u.setEmail(req.email());
        u.setPhone(req.phone());
        u.setRole(req.role());
        return UserResponse.from(repo.save(u));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("User not found");
        repo.deleteById(id);
    }
}
