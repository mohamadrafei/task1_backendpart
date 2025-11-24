package com.example.demo.web.dto;

import java.time.LocalDate;

import com.example.demo.domain.Entities.DailyReport;

public record DailyReportResponse(
        Long id,
        LocalDate date,
        Long totalJobs,
        Long newJobs,
        Long inProgressJobs,
        Long completedJobs,
        Long cancelledJobs,
        Long totalWorkTimeMinutes,
        Double totalMaterialCost) {
    public static DailyReportResponse from(DailyReport r) {
        return new DailyReportResponse(
                r.getId(),
                r.getDate(),
                r.getTotalJobs(),
                r.getNewJobs(),
                r.getInProgressJobs(),
                r.getCompletedJobs(),
                r.getCancelledJobs(),
                r.getTotalWorkTimeMinutes(),
                r.getTotalMaterialCost());
    }
}
