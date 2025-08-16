package com.zhansaya.lovelypets.controller;


import com.zhansaya.lovelypets.dto.seller.CreateSellerRequest;
import com.zhansaya.lovelypets.dto.seller.UpdateSellerRequest;
import com.zhansaya.lovelypets.dto.seller.SellerResponse;
import com.zhansaya.lovelypets.services.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService service;

    @PostMapping
    public SellerResponse create(@RequestBody @Valid CreateSellerRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public SellerResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public Page<SellerResponse> list(Pageable pageable) {
        return service.list(pageable);
    }

    @PutMapping("/{id}")
    public SellerResponse update(@PathVariable Long id, @RequestBody @Valid UpdateSellerRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
