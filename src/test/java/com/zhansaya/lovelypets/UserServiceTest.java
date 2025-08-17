package com.zhansaya.lovelypets;

import com.zhansaya.lovelypets.domain.enums.Role;
import com.zhansaya.lovelypets.domain.models.User;
import com.zhansaya.lovelypets.dto.user.CreateUserRequest;
import com.zhansaya.lovelypets.dto.user.UpdateUserRequest;
import com.zhansaya.lovelypets.dto.user.UserResponse;
import com.zhansaya.lovelypets.repositories.UserRepository;
import com.zhansaya.lovelypets.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    private CreateUserRequest createReq;

    @BeforeEach
    void setUp() {
        createReq = new CreateUserRequest(
                "Zhansaya",
                "Kalymova",
                "zhansaya@example.com",
                "77001234567",
                "RawP@ssw0rd",
                Role.CUSTOMER
        );
    }

    @Test
    void create_ok() {
        when(repo.existsByEmail(createReq.email())).thenReturn(false);
        when(repo.existsByPhone(createReq.phone())).thenReturn(false);
        when(passwordEncoder.encode(createReq.password())).thenReturn("ENCODED");
        var saved = user(1L, createReq.firstname(), createReq.lastname(),
                createReq.email(), createReq.phone(), "ENCODED", createReq.role());
        when(repo.save(any(User.class))).thenReturn(saved);

        UserResponse resp = service.create(createReq);

        assertEquals(1L, resp.id());
        assertEquals("Zhansaya", resp.firstname());
        assertEquals("Kalymova", resp.lastname());
        assertEquals("zhansaya@example.com", resp.email());
        assertEquals("77001234567", resp.phone());
        assertEquals(Role.CUSTOMER, resp.role());
        verify(passwordEncoder).encode("RawP@ssw0rd");
        verify(repo).save(argThat(u ->
                u.getFirstname().equals("Zhansaya") &&
                        u.getPassword().equals("ENCODED")
        ));
    }

    @Test
    void create_throws_whenEmailExists() {
        when(repo.existsByEmail(createReq.email())).thenReturn(true);
        var ex = assertThrows(DataIntegrityViolationException.class, () -> service.create(createReq));
        assertTrue(ex.getMessage().toLowerCase().contains("email"));
        verify(repo, never()).save(any());
    }

    @Test
    void create_throws_whenPhoneExists() {
        when(repo.existsByEmail(createReq.email())).thenReturn(false);
        when(repo.existsByPhone(createReq.phone())).thenReturn(true);
        var ex = assertThrows(DataIntegrityViolationException.class, () -> service.create(createReq));
        assertTrue(ex.getMessage().toLowerCase().contains("phone"));
        verify(repo, never()).save(any());
    }

    @Test
    void get_ok() {
        var u = user(10L, "A", "B", "a@b.com", "7700", "enc", Role.ADMIN);
        when(repo.findById(10L)).thenReturn(Optional.of(u));

        var resp = service.get(10L);

        assertEquals(10L, resp.id());
        assertEquals("A", resp.firstname());
        assertEquals(Role.ADMIN, resp.role());
    }

    @Test
    void get_throws_whenNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        var ex = assertThrows(IllegalArgumentException.class, () -> service.get(99L));
        assertTrue(ex.getMessage().toLowerCase().contains("user not found"));
    }

    @Test
    void list_mapsPageToDto() {
        var u1 = user(1L, "A", "A", "a@ex.com", "1", "p", Role.CUSTOMER);
        var u2 = user(2L, "B", "B", "b@ex.com", "2", "p", Role.SELLER);
        Page<User> page = new PageImpl<>(List.of(u1, u2), PageRequest.of(0, 2), 2);
        when(repo.findAll(any(Pageable.class))).thenReturn(page);

        Page<UserResponse> out = service.list(PageRequest.of(0, 2));

        assertEquals(2, out.getTotalElements());
        assertEquals("A", out.getContent().get(0).firstname());
        assertEquals(Role.SELLER, out.getContent().get(1).role());
        verify(repo).findAll(any(Pageable.class));
    }

    @Test
    void update_ok() {
        var current = user(5L, "Old", "Name", "old@ex.com", "700", "enc", Role.CUSTOMER);
        when(repo.findById(5L)).thenReturn(Optional.of(current));

        var req = new UpdateUserRequest(
                "New",
                "Surname",
                "new@ex.com",
                "701",
                Role.SELLER
        );

        when(repo.existsByEmail("new@ex.com")).thenReturn(false);
        when(repo.existsByPhone("701")).thenReturn(false);

        var saved = user(5L, "New", "Surname", "new@ex.com", "701", "enc", Role.SELLER);
        when(repo.save(any(User.class))).thenReturn(saved);

        var resp = service.update(5L, req);

        assertEquals(5L, resp.id());
        assertEquals("New", resp.firstname());
        assertEquals("701", resp.phone());
        assertEquals(Role.SELLER, resp.role());
    }

    @Test
    void update_throws_whenEmailTakenByAnother() {
        var current = user(5L, "Old", "Name", "old@ex.com", "700", "enc", Role.CUSTOMER);
        when(repo.findById(5L)).thenReturn(Optional.of(current));

        var req = new UpdateUserRequest("n","n","taken@ex.com","700", Role.CUSTOMER);

        when(repo.existsByEmail("taken@ex.com")).thenReturn(true);

        var ex = assertThrows(DataIntegrityViolationException.class, () -> service.update(5L, req));
        assertTrue(ex.getMessage().toLowerCase().contains("email"));
        verify(repo, never()).save(any());
    }

    @Test
    void update_throws_whenPhoneTakenByAnother() {
        var current = user(5L, "Old", "Name", "old@ex.com", "700", "enc", Role.CUSTOMER);
        when(repo.findById(5L)).thenReturn(Optional.of(current));

        var req = new UpdateUserRequest("n","n","old@ex.com","999", Role.CUSTOMER);
        when(repo.existsByPhone("999")).thenReturn(true);

        var ex = assertThrows(DataIntegrityViolationException.class, () -> service.update(5L, req));
        assertTrue(ex.getMessage().toLowerCase().contains("phone"));
        verify(repo, never()).save(any());
    }

    @Test
    void delete_ok() {
        when(repo.existsById(7L)).thenReturn(true);

        service.delete(7L);

        verify(repo).deleteById(7L);
    }

    @Test
    void delete_throws_whenNotFound() {
        when(repo.existsById(77L)).thenReturn(false);

        var ex = assertThrows(IllegalArgumentException.class, () -> service.delete(77L));
        assertTrue(ex.getMessage().toLowerCase().contains("user not found"));
        verify(repo, never()).deleteById(anyLong());
    }
    private static User user(Long id, String fn, String ln, String email, String phone, String pass, Role role) {
        return User.builder()
                .id(id)
                .firstname(fn)
                .lastname(ln)
                .email(email)
                .phone(phone)
                .password(pass)
                .role(role)
                .build();
    }
}

