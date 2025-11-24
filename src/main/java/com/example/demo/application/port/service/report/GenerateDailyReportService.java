package com.example.demo.application.port.service.report;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.port.in.DailyReportQueryUseCase;
import com.example.demo.application.port.in.GenerateDailyReportUseCase;
import com.example.demo.domain.Entities.DailyReport;
import com.example.demo.domain.Entities.Job;
import com.example.demo.domain.Entities.MaterialUsage;
import com.example.demo.domain.Enums.JobStatus;
import com.example.demo.infrastructure.persistence.repository.DailyJobReportJpaRepository;
import com.example.demo.infrastructure.persistence.repository.JobJpaRepository;
import com.example.demo.infrastructure.persistence.repository.MaterialUsageJpaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GenerateDailyReportService
        implements GenerateDailyReportUseCase, DailyReportQueryUseCase {

    private final JobJpaRepository jobJpaRepository;
    private final MaterialUsageJpaRepository materialUsageJpaRepository;
    private final DailyJobReportJpaRepository dailyJobReportJpaRepository;

    @Override
    public void generateForDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        // 1) Get all jobs scheduled on this date (any company, any technician)
        List<Job> jobs = jobJpaRepository.findByScheduledStartBetween(startOfDay, endOfDay);

        if (jobs.isEmpty()) {
            // no jobs that day → nothing to do
            return;
        }

        // Group by companyId
        Map<Long, List<Job>> jobsByCompany = jobs.stream()
                .filter(j -> j.getCompanyId() != null)
                .collect(Collectors.groupingBy(Job::getCompanyId));

        for (Map.Entry<Long, List<Job>> entry : jobsByCompany.entrySet()) {
            Long companyId = entry.getKey();
            List<Job> companyJobs = entry.getValue();

            long totalJobs = companyJobs.size();
            long newJobs = companyJobs.stream()
                    .filter(j -> j.getStatus() == JobStatus.NEW).count();
            long inProgressJobs = companyJobs.stream()
                    .filter(j -> j.getStatus() == JobStatus.IN_PROGRESS).count();
            long completedJobs = companyJobs.stream()
                    .filter(j -> j.getStatus() == JobStatus.COMPLETED).count();
            long cancelledJobs = companyJobs.stream()
                    .filter(j -> j.getStatus() == JobStatus.CANCELLED).count();

            long totalMinutes = companyJobs.stream()
                    .filter(j -> j.getWorkStartedAt() != null && j.getWorkCompletedAt() != null)
                    .mapToLong(j -> Duration.between(
                            j.getWorkStartedAt(),
                            j.getWorkCompletedAt()).toMinutes())
                    .sum();

            // collect job IDs for this company
            List<Long> jobIds = companyJobs.stream()
                    .map(Job::getId)
                    .toList();

            // ⚠️ NOTE: this assumes your MaterialUsageJpaRepository
            // has a method like `findByJobIdIn(List<Long> jobIds)`
            List<MaterialUsage> materials = materialUsageJpaRepository.findByJobIdIn(jobIds);

            double totalMaterialCost = materials.stream()
                    .mapToDouble(m -> {
                        double qty = m.getQuantity() != null ? m.getQuantity() : 0.0;
                        java.math.BigDecimal unitPrice = m.getUnitPrice() != null ? m.getUnitPrice()
                                : java.math.BigDecimal.ZERO;
                        java.math.BigDecimal qtyBd = java.math.BigDecimal.valueOf(qty);
                        return qtyBd.multiply(unitPrice).doubleValue();
                    })
                    .sum();

            DailyReport report = dailyJobReportJpaRepository
                    .findByCompanyIdAndDate(companyId, date)
                    .orElseGet(DailyReport::new);

            report.setCompanyId(companyId);
            report.setDate(date);
            report.setTotalJobs(totalJobs);
            report.setNewJobs(newJobs);
            report.setInProgressJobs(inProgressJobs);
            report.setCompletedJobs(completedJobs);
            report.setCancelledJobs(cancelledJobs);
            report.setTotalWorkTimeMinutes(totalMinutes);
            report.setTotalMaterialCost(totalMaterialCost);

            dailyJobReportJpaRepository.save(report);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DailyReport getByCompanyAndDate(Long companyId, LocalDate date) {
        return dailyJobReportJpaRepository.findByCompanyIdAndDate(companyId, date)
                .orElseThrow(() -> new EntityNotFoundException("No daily report for this date"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyReport> getByCompanyAndRange(Long companyId, LocalDate start, LocalDate end) {
        return dailyJobReportJpaRepository.findByCompanyIdAndDateBetween(companyId, start, end);
    }
}
