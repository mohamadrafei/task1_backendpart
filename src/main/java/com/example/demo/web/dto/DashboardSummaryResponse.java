package com.example.demo.web.dto;

public record DashboardSummaryResponse(
        long totalJobs,
        long jobsToday,
        long inProgressJobs,
        long completedToday,
        long totalTechnicians,
        double totalMaterialsCostToday) {
}
