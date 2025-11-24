package com.example.demo.web.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Entities.User;
import com.example.demo.infrastructure.persistence.repository.UserRepository;
import com.example.demo.infrastructure.security.JwtProvider;
import com.example.demo.web.dto.AuthResponse;
import com.example.demo.web.dto.LoginRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        String raw = request.password();
        String stored = user.getPasswordHash();

        // ✅ accept both encoded + plain (TEMP for development)
        boolean matches = (stored != null && stored.startsWith("$2a$")) // looks like bcrypt
                ? passwordEncoder.matches(raw, stored) // use bcrypt
                : raw.equals(stored); // plain text compare

        if (!matches) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtProvider.generateToken(user);

        var roles = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        return new AuthResponse(
                token,
                user.getId(),
                user.getCompanyId(),
                roles);
    }

}
