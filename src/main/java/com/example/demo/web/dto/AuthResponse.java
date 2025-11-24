package com.example.demo.web.dto;

import java.util.Set;

public record AuthResponse(
                String token,
                Long userId,
                Long companyId,
                Set<String> roles) {
}
