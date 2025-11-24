package com.example.demo.web.dto;

import java.math.BigDecimal;

public record AddMaterialRequest(
        String name,
        Double quantity,
        String unit,
        BigDecimal unitPrice) {
}
