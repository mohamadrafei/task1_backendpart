package com.example.demo.application.port.service.report;

import com.example.demo.application.port.in.DailyReportUseCase;
import com.example.demo.application.port.out.JobRepositoryPort;
import com.example.demo.application.port.out.MaterialUsageRepository;
import com.example.demo.domain.Entities.Job;
import com.example.demo.domain.Enums.JobStatus;
import com.example.demo.infrastructure.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyReportService implements DailyReportUseCase {

    private final JobRepositoryPort jobRepository;
    private final MaterialUsageRepository materialUsageRepository;

    @Override
    public byte[] generateDailyReportCsv(Long companyId, LocalDate date) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        // -----------------------
        // 1. Fetch Summary Values
        // -----------------------
        long totalJobs = jobRepository.countByCompanyId(companyId);

        long jobsToday = jobRepository.countCompletedForDay(
                companyId, start, end);

        long inProgressJobs = jobRepository.countByCompanyIdAndStatus(
                companyId, JobStatus.IN_PROGRESS);

        long completedToday = jobRepository.countCompletedForDay(
                companyId, start, end);

        double totalMaterialsCostToday = materialUsageRepository.totalMaterialsCostForDay(companyId, start, end);

        // -----------------------
        // 2. Fetch Job Details
        // -----------------------
        List<Job> jobs = jobRepository
                .findAllByCompanyIdAndScheduledStartBetween(companyId, start, end);

        // -----------------------
        // 3. Build CSV
        // -----------------------
        StringBuilder sb = new StringBuilder();

        // A) SUMMARY SECTION
        sb.append(
                "Date,Company ID,Total Jobs,Jobs Today,In Progress Jobs,Completed Today,Total Technicians,Materials Cost Today\n");
        sb.append(date).append(",");
        sb.append(companyId).append(",");
        sb.append(totalJobs).append(",");
        sb.append(jobsToday).append(",");
        sb.append(inProgressJobs).append(",");
        sb.append(completedToday).append(",");
        sb.append(totalMaterialsCostToday).append("\n\n");

        // B) JOB DETAILS SECTION
        sb.append(
                "Job ID,Title,Status,Assigned Technician,Scheduled Start,Scheduled End,Work Started,Work Completed\n");

        for (Job job : jobs) {
            sb.append(job.getId()).append(",");
            sb.append(escapeCsv(job.getTitle())).append(",");
            sb.append(job.getStatus()).append(",");
            sb.append(job.getAssignedTechnicianId() != null ? job.getAssignedTechnicianId() : "").append(",");
            sb.append(job.getScheduledStart() != null ? job.getScheduledStart() : "").append(",");
            sb.append(job.getScheduledEnd() != null ? job.getScheduledEnd() : "").append(",");
            sb.append(job.getWorkStartedAt() != null ? job.getWorkStartedAt() : "").append(",");
            sb.append(job.getWorkCompletedAt() != null ? job.getWorkCompletedAt() : "").append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escapeCsv(String value) {
        if (value == null)
            return "";
        String v = value.replace("\"", "\"\"");
        if (v.contains(",") || v.contains("\n")) {
            return "\"" + v + "\"";
        }
        return v;
    }

}
