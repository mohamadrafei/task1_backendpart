package com.example.demo.application.port.in;

import java.math.BigDecimal;

public record AddMaterialCommand(
        Long jobId,
        Long technicianId,
        String name,
        Double quantity,
        String unit,
        BigDecimal unitPrice) {
}
