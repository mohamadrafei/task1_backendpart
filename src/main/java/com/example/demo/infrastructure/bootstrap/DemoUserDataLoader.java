package com.example.demo.infrastructure.bootstrap;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Entities.User;
import com.example.demo.domain.Enums.Role;
import com.example.demo.infrastructure.persistence.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DemoUserDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User technician = new User();
            technician.setName("Tech One");
            technician.setEmail("tech@example.com");
            technician.setCompanyId(1L);
            technician.setPasswordHash(passwordEncoder.encode("password")); // 👈 raw = password
            technician.setRoles(Set.of(Role.TECHNICIAN));

            userRepository.save(technician);
        }
    }
}
