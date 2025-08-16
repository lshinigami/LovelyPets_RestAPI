package com.zhansaya.lovelypets.controller;

import com.zhansaya.lovelypets.dto.user.CreateUserRequest;
import com.zhansaya.lovelypets.dto.user.UpdateUserRequest;
import com.zhansaya.lovelypets.dto.user.UserResponse;
import com.zhansaya.lovelypets.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public UserResponse create(@RequestBody @Valid CreateUserRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public Page<UserResponse> list(Pageable pageable) {
        return service.list(pageable);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
