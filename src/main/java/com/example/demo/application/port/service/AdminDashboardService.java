package com.example.demo.application.port.service;

import com.example.demo.application.port.in.AdminDashboardUseCase;
import com.example.demo.application.port.out.JobRepositoryPort;
import com.example.demo.application.port.out.MaterialUsageRepository;
import com.example.demo.domain.Enums.JobStatus;
import com.example.demo.infrastructure.persistence.repository.UserRepository;
import com.example.demo.web.dto.DashboardSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminDashboardService implements AdminDashboardUseCase {

    private final JobRepositoryPort jobRepository;
    private final UserRepository userRepository;
    private final MaterialUsageRepository materialUsageRepository;

    @Override
    public DashboardSummaryResponse getSummary(Long companyId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        long totalJobs = jobRepository.countByCompanyId(companyId);
        long jobsToday = jobRepository.countJobsForDay(companyId, startOfDay, endOfDay);
        long inProgressJobs = jobRepository.countByCompanyIdAndStatus(companyId, JobStatus.IN_PROGRESS);

        long completedToday = jobRepository.countCompletedForDay(companyId, startOfDay, endOfDay);

        long totalTechnicians = userRepository.countTechniciansForCompany(companyId);

        double totalMaterialsCostToday = materialUsageRepository.totalMaterialsCostForDay(companyId, startOfDay,
                endOfDay);

        return new DashboardSummaryResponse(
                totalJobs,
                jobsToday,
                inProgressJobs,
                completedToday,
                totalTechnicians,
                totalMaterialsCostToday);
    }
}
