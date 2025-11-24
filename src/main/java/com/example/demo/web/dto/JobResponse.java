package com.example.demo.web.dto;

public record JobResponse(
        Long id,
        String title,
        String description,
        String status,
        String priority,
        String address,
        Long assignedTechnicianId) {
}
