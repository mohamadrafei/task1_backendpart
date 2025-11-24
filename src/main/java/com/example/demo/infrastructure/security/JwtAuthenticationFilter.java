package com.example.demo.infrastructure.security;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.domain.Entities.User;
import com.example.demo.domain.Enums.Role;
import com.example.demo.infrastructure.persistence.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // ⬇️ VERY IMPORTANT: don't try JWT for login/register
        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                Claims claims = jwtProvider.parseClaims(token);

                Long userId = Long.parseLong(claims.getSubject());

                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    Set<Role> roles = user.getRoles();
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                            .collect(Collectors.toList());

                    var authentication = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ignored) {
                // invalid token -> leave unauthenticated
            }
        }

        filterChain.doFilter(request, response);
    }
}
