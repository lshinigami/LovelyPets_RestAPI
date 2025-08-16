package com.zhansaya.lovelypets.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhansaya.lovelypets.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(name = "user_firstname", nullable = false, length = 50)
    String firstname;

    @Column(name = "user_lastname", nullable = false, length = 50)
    String lastname;

    @Column(name = "user_email", nullable = false, length = 50)
    String email;

    @Column(name = "user_phone", nullable = false, length = 20)
    String phone;

    @JsonIgnore
    @Column(name = "user_password", nullable = false)
    String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, length = 50)
    Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
