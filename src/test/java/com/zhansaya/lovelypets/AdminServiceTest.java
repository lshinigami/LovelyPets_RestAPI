package com.zhansaya.lovelypets;

import com.zhansaya.lovelypets.domain.enums.Role;
import com.zhansaya.lovelypets.domain.models.Admin;
import com.zhansaya.lovelypets.domain.models.User;
import com.zhansaya.lovelypets.dto.admin.AdminResponse;
import com.zhansaya.lovelypets.dto.admin.CreateAdminRequest;
import com.zhansaya.lovelypets.repositories.AdminRepository;
import com.zhansaya.lovelypets.repositories.UserRepository;
import com.zhansaya.lovelypets.services.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    AdminRepository adminRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    AdminService service;

    private static User user(Long id, Role role) {
        return User.builder()
                .id(id)
                .firstname("A")
                .lastname("B")
                .email("a@b.com")
                .phone("7700")
                .password("enc")
                .role(role)
                .build();
    }

    private static Admin admin(Long id, User u, boolean active, String resp) {
        return Admin.builder()
                .id(id)
                .user(u)
                .isActive(active)
                .responsibleFor(resp)
                .build();
    }

    @Test
    void addAdmin_ok() {
        var u = user(5L, Role.CUSTOMER);
        var req = new CreateAdminRequest(5L, "Manage categories", true);

        when(userRepository.findById(5L)).thenReturn(Optional.of(u));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(adminRepository.save(any(Admin.class))).thenAnswer(inv -> {
            Admin a = inv.getArgument(0);
            a.setId(11L);
            return a;
        });

        AdminResponse resp = service.addAdmin(req);

        assertEquals(11L, resp.id());
        assertEquals(5L, resp.userId());
        assertEquals("Manage categories", resp.responsibleFor());
        assertEquals(true, resp.isActive());
        assertEquals(Role.ADMIN, u.getRole());
        verify(adminRepository).save(argThat(a ->
                a.getUser().getId().equals(5L) &&
                        Boolean.TRUE.equals(a.getIsActive()) &&
                        "Manage categories".equals(a.getResponsibleFor())
        ));
    }

    @Test
    void addAdmin_userNotFound() {
        var req = new CreateAdminRequest(99L, "Resp", true);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.addAdmin(req));
        verify(adminRepository, never()).save(any());
    }

    @Test
    void deactivate_ok() {
        var u = user(1L, Role.ADMIN);
        var a = admin(7L, u, true, "Resp");
        when(adminRepository.findById(7L)).thenReturn(Optional.of(a));
        when(adminRepository.save(any(Admin.class))).thenAnswer(inv -> inv.getArgument(0));

        service.deactivate(7L);

        assertFalse(a.getIsActive());
        verify(adminRepository).save(a);
    }

    @Test
    void deactivate_notFound() {
        when(adminRepository.findById(77L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.deactivate(77L));
        verify(adminRepository, never()).save(any());
    }

    @Test
    void changeResponsibilities_ok() {
        var u = user(2L, Role.ADMIN);
        var a = admin(8L, u, true, "Old");
        when(adminRepository.findById(8L)).thenReturn(Optional.of(a));
        when(adminRepository.save(any(Admin.class))).thenAnswer(inv -> inv.getArgument(0));

        AdminResponse resp = service.changeResponsibilities(8L, "New Resp");

        assertEquals("New Resp", resp.responsibleFor());
        verify(adminRepository).save(a);
    }

    @Test
    void changeResponsibilities_notFound() {
        when(adminRepository.findById(88L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.changeResponsibilities(88L, "New"));
        verify(adminRepository, never()).save(any());
    }

    @Test
    void getActiveAdmins_ok() {
        var u = user(1L, Role.ADMIN);
        var a1 = admin(1L, u, true, "R1");
        var a2 = admin(2L, u, true, "R2");
        when(adminRepository.findByIsActiveTrue()).thenReturn(List.of(a1, a2));

        var list = service.getActiveAdmins();

        assertEquals(2, list.size());
        assertTrue(list.stream().allMatch(Admin::getIsActive));
    }

    @Test
    void findById_ok() {
        var u = user(3L, Role.ADMIN);
        var a = admin(9L, u, true, "R");
        when(adminRepository.findById(9L)).thenReturn(Optional.of(a));

        var opt = service.findById(9L);

        assertTrue(opt.isPresent());
        assertEquals(9L, opt.get().getId());
    }

    @Test
    void findById_empty() {
        when(adminRepository.findById(123L)).thenReturn(Optional.empty());
        assertTrue(service.findById(123L).isEmpty());
    }
}

