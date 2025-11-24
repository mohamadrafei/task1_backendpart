package com.example.demo.web.dto;

public record UpdateJobStatusRequest(
        String status // e.g. "IN_PROGRESS", "COMPLETED", "ON_HOLD"
) {
}
