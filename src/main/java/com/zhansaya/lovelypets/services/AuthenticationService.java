package com.zhansaya.lovelypets.services;

import com.zhansaya.lovelypets.domain.enums.Role;
import com.zhansaya.lovelypets.domain.exceptions.AuthenticationFailedException;
import com.zhansaya.lovelypets.domain.models.User;
import com.zhansaya.lovelypets.repositories.UserRepository;
import com.zhansaya.lovelypets.request.AuthenticationRequest;
import com.zhansaya.lovelypets.request.RegisterRequest;
import com.zhansaya.lovelypets.response.AuthenticationResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationService {

    final UserRepository repository;
    final PasswordEncoder encoder;
    final JwtService jwtService;
    final AuthenticationManager authManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(encoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getPhone(), request.getPassword()
        ));
        var user = repository.findByPhone(request.getPhone()).orElseThrow(
                () -> new AuthenticationFailedException("User not found")
        );

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
