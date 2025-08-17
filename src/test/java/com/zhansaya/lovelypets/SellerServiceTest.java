package com.zhansaya.lovelypets;

import com.zhansaya.lovelypets.domain.enums.OrganizationType;
import com.zhansaya.lovelypets.domain.enums.Role;
import com.zhansaya.lovelypets.domain.models.Seller;
import com.zhansaya.lovelypets.domain.models.User;
import com.zhansaya.lovelypets.dto.seller.CreateSellerRequest;
import com.zhansaya.lovelypets.dto.seller.SellerResponse;
import com.zhansaya.lovelypets.dto.seller.UpdateSellerRequest;
import com.zhansaya.lovelypets.repositories.SellerRepository;
import com.zhansaya.lovelypets.repositories.UserRepository;
import com.zhansaya.lovelypets.services.SellerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @Mock
    SellerRepository sellerRepo;
    @Mock
    UserRepository userRepo;

    @InjectMocks
    SellerService service;

    private static User user(Long id) {
        return User.builder()
                .id(id)
                .firstname("First")
                .lastname("Last")
                .email("u"+id+"@ex.com")
                .phone("7700"+id)
                .password("enc")
                .role(Role.SELLER)
                .build();
    }

    private static Seller seller(Long id, User u, String org, OrganizationType type) {
        Seller s = Seller.builder()
                .id(id)
                .user(u)
                .organizationName(org)
                .organizationType(type)
                .iin("123456789012")
                .bin(null)
                .build();
        try {
            s.setViolationsCount(0);
            s.setVerified(false);
        } catch (Exception ignore) {}
        return s;
    }

    @Test
    void create_ok() {
        var owner = user(10L);
        var req = new CreateSellerRequest(10L, "Lovely Pets", OrganizationType.TOO, "123456789012", null);

        when(userRepo.findById(10L)).thenReturn(Optional.of(owner));
        when(sellerRepo.existsByUserIdAndOrganizationNameIgnoreCase(10L, "Lovely Pets")).thenReturn(false);

        var saved = seller(1L, owner, "Lovely Pets", OrganizationType.TOO);
        when(sellerRepo.save(any(Seller.class))).thenReturn(saved);

        SellerResponse resp = service.create(req);

        assertEquals(1L, resp.id());
        assertEquals(10L, resp.userId());
        assertEquals("Lovely Pets", resp.organizationName());
        assertEquals(OrganizationType.TOO, resp.organizationType());

        verify(sellerRepo).save(argThat(s ->
                s.getUser().getId().equals(10L) &&
                        s.getOrganizationName().equals("Lovely Pets")
        ));
    }

    @Test
    void create_throws_whenUserNotFound() {
        var req = new CreateSellerRequest(99L, "Shop", OrganizationType.IP, null, null);
        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        var ex = assertThrows(IllegalArgumentException.class, () -> service.create(req));
        assertTrue(ex.getMessage().toLowerCase().contains("user not found"));
        verify(sellerRepo, never()).save(any());
    }

    @Test
    void create_throws_whenDuplicateOrgForUser() {
        var owner = user(10L);
        var req = new CreateSellerRequest(10L, "SameName", OrganizationType.TOO, null, null);

        when(userRepo.findById(10L)).thenReturn(Optional.of(owner));
        when(sellerRepo.existsByUserIdAndOrganizationNameIgnoreCase(10L, "SameName")).thenReturn(true);

        var ex = assertThrows(DataIntegrityViolationException.class, () -> service.create(req));
        assertTrue(ex.getMessage().toLowerCase().contains("organizationname"));
        verify(sellerRepo, never()).save(any());
    }

    @Test
    void get_ok() {
        var u = user(1L);
        var s = seller(5L, u, "Shop", OrganizationType.IP);
        when(sellerRepo.findById(5L)).thenReturn(Optional.of(s));

        var resp = service.get(5L);

        assertEquals(5L, resp.id());
        assertEquals("Shop", resp.organizationName());
    }

    @Test
    void get_notFound() {
        when(sellerRepo.findById(777L)).thenReturn(Optional.empty());
        var ex = assertThrows(IllegalArgumentException.class, () -> service.get(777L));
        assertTrue(ex.getMessage().toLowerCase().contains("seller not found"));
    }

    @Test
    void list_mapsPage() {
        var u = user(1L);
        Page<Seller> page = new PageImpl<>(List.of(
                seller(1L, u, "A", OrganizationType.IP),
                seller(2L, u, "B", OrganizationType.TOO)
        ), PageRequest.of(0, 2), 2);

        when(sellerRepo.findAll(any(Pageable.class))).thenReturn(page);

        Page<SellerResponse> out = service.list(PageRequest.of(0, 2));

        assertEquals(2, out.getTotalElements());
        assertEquals("A", out.getContent().get(0).organizationName());
        assertEquals(OrganizationType.TOO, out.getContent().get(1).organizationType());
    }

    @Test
    void update_ok() {
        var u = user(7L);
        var current = seller(20L, u, "OldName", OrganizationType.IP);
        when(sellerRepo.findById(20L)).thenReturn(Optional.of(current));
        when(sellerRepo.existsByUserIdAndOrganizationNameIgnoreCase(7L, "NewName")).thenReturn(false);

        var req = new UpdateSellerRequest("NewName", OrganizationType.TOO, "111111111111", null);
        when(sellerRepo.save(any(Seller.class)))
                .thenAnswer(inv -> inv.getArgument(0, Seller.class));

        var resp = service.update(20L, req);

        assertEquals(20L, resp.id());
        assertEquals("NewName", resp.organizationName());
        assertEquals(OrganizationType.TOO, resp.organizationType());
    }

    @Test
    void update_throws_whenDuplicateName() {
        var u = user(7L);
        var current = seller(20L, u, "OldName", OrganizationType.IP);
        when(sellerRepo.findById(20L)).thenReturn(Optional.of(current));
        when(sellerRepo.existsByUserIdAndOrganizationNameIgnoreCase(7L, "Clash")).thenReturn(true);

        var req = new UpdateSellerRequest("Clash", OrganizationType.TOO, null, null);

        var ex = assertThrows(DataIntegrityViolationException.class, () -> service.update(20L, req));
        assertTrue(ex.getMessage().toLowerCase().contains("organizationname"));
        verify(sellerRepo, never()).save(any());
    }

    @Test
    void delete_ok() {
        when(sellerRepo.existsById(33L)).thenReturn(true);
        service.delete(33L);
        verify(sellerRepo).deleteById(33L);
    }

    @Test
    void delete_notFound() {
        when(sellerRepo.existsById(33L)).thenReturn(false);
        var ex = assertThrows(IllegalArgumentException.class, () -> service.delete(33L));
        assertTrue(ex.getMessage().toLowerCase().contains("seller not found"));
        verify(sellerRepo, never()).deleteById(anyLong());
    }

    @Test
    void addViolation_increments_whenLessThan3() {
        var u = user(1L);
        var s = seller(10L, u, "Shop", OrganizationType.IP);
        s.setViolationsCount(1);

        when(sellerRepo.findById(10L)).thenReturn(Optional.of(s));

        service.addViolationOrDelete(10L);

        assertEquals(2, s.getViolationsCount());
        verify(sellerRepo, never()).delete(any(Seller.class));
        verify(sellerRepo, atLeastOnce()).findById(10L);
    }

    @Test
    void addViolation_deletes_whenBecomes3() {
        var u = user(1L);
        var s = seller(10L, u, "Shop", OrganizationType.IP);
        s.setViolationsCount(2);

        when(sellerRepo.findById(10L)).thenReturn(Optional.of(s));

        service.addViolationOrDelete(10L);

        verify(sellerRepo).delete(s);
    }
}

