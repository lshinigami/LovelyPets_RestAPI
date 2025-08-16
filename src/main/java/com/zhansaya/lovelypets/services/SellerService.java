package com.zhansaya.lovelypets.services;

import com.zhansaya.lovelypets.dto.seller.CreateSellerRequest;
import com.zhansaya.lovelypets.dto.seller.UpdateSellerRequest;
import com.zhansaya.lovelypets.dto.seller.SellerResponse;
import com.zhansaya.lovelypets.domain.models.Seller;
import com.zhansaya.lovelypets.domain.models.User;
import com.zhansaya.lovelypets.repositories.SellerRepository;
import com.zhansaya.lovelypets.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SellerService {

    private final SellerRepository sellerRepo;
    private final UserRepository userRepo;

    public SellerResponse create(CreateSellerRequest req) {
        User owner = userRepo.findById(req.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (sellerRepo.existsByUserIdAndOrganizationNameIgnoreCase(owner.getId(), req.organizationName())) {
            throw new DataIntegrityViolationException("Seller with this organizationName already exists for the user");
        }

        Seller s = Seller.builder()
                .user(owner)
                .organizationName(req.organizationName())
                .organizationType(req.organizationType())
                .iin(req.iin())
                .bin(req.bin())
                .build();

        return SellerResponse.from(sellerRepo.save(s));
    }

    @Transactional(readOnly = true)
    public SellerResponse get(Long id) {
        Seller s = sellerRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));
        return SellerResponse.from(s);
    }

    @Transactional(readOnly = true)
    public Page<SellerResponse> list(Pageable pageable) {
        return sellerRepo.findAll(pageable).map(SellerResponse::from);
    }

    public SellerResponse update(Long id, UpdateSellerRequest req) {
        Seller s = sellerRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        if (!s.getOrganizationName().equalsIgnoreCase(req.organizationName())
                && sellerRepo.existsByUserIdAndOrganizationNameIgnoreCase(s.getUser().getId(), req.organizationName())) {
            throw new DataIntegrityViolationException("Seller with this organizationName already exists for the user");
        }

        s.setOrganizationName(req.organizationName());
        s.setOrganizationType(req.organizationType());
        s.setIin(req.iin());
        s.setBin(req.bin());

        return SellerResponse.from(sellerRepo.save(s));
    }

    public void delete(Long id) {
        if (!sellerRepo.existsById(id)) throw new IllegalArgumentException("Seller not found");
        sellerRepo.deleteById(id);
    }
}

