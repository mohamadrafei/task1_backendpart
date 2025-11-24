package com.example.demo.web.dto;

import java.time.LocalDateTime;

public record CreateJobRequest(
        String title,
        String description,
        String priority,
        String address,
        Double latitude,
        Double longitude,
        LocalDateTime scheduledStart,
        LocalDateTime scheduledEnd) {
}
