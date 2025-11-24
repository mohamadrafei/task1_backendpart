package com.example.demo.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MaterialUsageResponse(
        Long id,
        Long jobId,
        Long technicianId,
        String name,
        Double quantity,
        String unit,
        BigDecimal unitPrice,
        LocalDateTime createdAt) {
}
