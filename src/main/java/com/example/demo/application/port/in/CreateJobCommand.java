package com.example.demo.application.port.in;

import java.time.LocalDateTime;

public record CreateJobCommand(
                Long companyId,
                String title,
                String description,
                String priority,
                String address,
                Double latitude,
                Double longitude,
                LocalDateTime scheduledStart,
                LocalDateTime scheduledEnd,
                Long createdByUserId) {
}
