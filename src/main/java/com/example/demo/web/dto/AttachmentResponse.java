package com.example.demo.web.dto;

import java.time.LocalDateTime;

public record AttachmentResponse(
        Long id,
        String url,
        String type,
        LocalDateTime createdAt) {
}
