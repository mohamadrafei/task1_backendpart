package com.example.demo.infrastructure.security;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.domain.Entities.User;
import com.example.demo.domain.Enums.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    // For test task this is fine. In real project use env variable.
    private final String secret = "this-is-a-very-long-secret-key-for-jwt-demo-1234567890";
    private final long expirationMs = 24 * 60 * 60 * 1000L; // 1 day

    public String generateToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        var roles = user.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("companyId", user.getCompanyId())
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
