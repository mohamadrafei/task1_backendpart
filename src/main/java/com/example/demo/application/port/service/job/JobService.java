package com.example.demo.application.port.service.job;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.application.port.in.AssignJobUseCase;
import com.example.demo.application.port.in.CreateJobCommand;
import com.example.demo.application.port.in.CreateJobUseCase;
import com.example.demo.application.port.in.GetJobUseCase;
import com.example.demo.application.port.in.GetJobsForTechnicianUseCase;
import com.example.demo.application.port.in.JobTimeTrackingUseCase;

import com.example.demo.application.port.out.JobRepositoryPort;
import com.example.demo.domain.Entities.Job;
import com.example.demo.domain.Enums.JobStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobService implements
        CreateJobUseCase,
        AssignJobUseCase,
        GetJobUseCase,
        GetJobsForTechnicianUseCase,
        JobTimeTrackingUseCase {

    private final JobRepositoryPort jobRepositoryPort;

    // --- CREATE JOB ---
    @Override
    public Job createJob(CreateJobCommand cmd) {
        Job job = new Job();
        job.setCompanyId(cmd.companyId());
        job.setTitle(cmd.title());
        job.setDescription(cmd.description());
        job.setPriority(cmd.priority());
        job.setAddress(cmd.address());
        job.setLatitude(cmd.latitude());
        job.setLongitude(cmd.longitude());
        job.setScheduledStart(cmd.scheduledStart());
        job.setScheduledEnd(cmd.scheduledEnd());
        job.setCreatedByUserId(cmd.createdByUserId());
        job.setStatus(JobStatus.NEW);

        return jobRepositoryPort.save(job);
    }

    @Override
    public void startWork(Long jobId, Long technicianId) {
        System.out.println("========== SERVICE: START WORK ==========");
        System.out.println("🔍 Job ID: " + jobId);
        System.out.println("🔍 Technician ID: " + technicianId);

        Job job = jobRepositoryPort.findById(jobId)
                .orElseThrow(() -> {
                    System.out.println("❌ Job not found");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found");
                });

        System.out.println("✅ Job found");
        System.out.println("   - Job title: " + job.getTitle());
        System.out.println("   - Job status: " + job.getStatus());
        System.out.println("   - Assigned technician ID: " + job.getAssignedTechnicianId());
        System.out.println("   - Matches current user: "
                + (job.getAssignedTechnicianId() != null && job.getAssignedTechnicianId().equals(technicianId)));

        if (job.getAssignedTechnicianId() == null) {
            System.out.println("❌ Job not assigned to anyone");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Job is not assigned");
        }

        if (!job.getAssignedTechnicianId().equals(technicianId)) {
            System.out.println("❌ Technician mismatch!");
            System.out.println("   Expected: " + job.getAssignedTechnicianId());
            System.out.println("   Got: " + technicianId);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not assigned to this job");
        }

        if (job.getStatus() != JobStatus.ASSIGNED) {
            System.out.println("❌ Wrong status: " + job.getStatus());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Job is not in assigned status. Current: " + job.getStatus());
        }

        System.out.println("✅ All checks passed, updating job...");
        job.setStatus(JobStatus.IN_PROGRESS);
        job.setWorkStartedAt(LocalDateTime.now());
        jobRepositoryPort.save(job);
        System.out.println("✅ Job updated successfully");
        System.out.println("=========================================");
    }

    @Override
    public void completeWork(Long jobId, Long technicianId) {
        System.out.println("========== SERVICE: COMPLETE WORK ==========");
        System.out.println("🔍 Job ID: " + jobId);
        System.out.println("🔍 Technician ID: " + technicianId);

        Job job = jobRepositoryPort.findById(jobId)
                .orElseThrow(() -> {
                    System.out.println("❌ Job not found");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found");
                });

        System.out.println("✅ Job found");
        System.out.println("   - Job status: " + job.getStatus());
        System.out.println("   - Assigned technician ID: " + job.getAssignedTechnicianId());

        if (!job.getAssignedTechnicianId().equals(technicianId)) {
            System.out.println("❌ Technician mismatch!");
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not assigned to this job");
        }

        if (job.getStatus() != JobStatus.IN_PROGRESS) {
            System.out.println("❌ Wrong status: " + job.getStatus());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Job is not in progress. Current: " + job.getStatus());
        }

        System.out.println("✅ All checks passed, updating job...");
        job.setStatus(JobStatus.COMPLETED);
        job.setWorkCompletedAt(LocalDateTime.now());
        jobRepositoryPort.save(job);
        System.out.println("✅ Job completed successfully");
        System.out.println("============================================");
    }

    // --- ASSIGN JOB TO TECHNICIAN ---
    @Override
    public void assignJob(Long jobId, Long technicianId, Long assignedByUserId) {
        Job job = jobRepositoryPort.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));

        job.setAssignedTechnicianId(technicianId);
        job.setStatus(JobStatus.ASSIGNED);

        jobRepositoryPort.save(job);
    }

    // --- GET JOB BY ID ---
    @Override
    public Job getJob(Long jobId) {
        return jobRepositoryPort.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));
    }

    // --- GET TODAY JOBS FOR TECHNICIAN ---
    @Override
    public List<Job> getJobsForDate(Long technicianId, LocalDate date) {
        // This uses the method we already defined in JobRepositoryPort
        return jobRepositoryPort.findByAssignedTechnicianAndDate(technicianId, date);
    }

    @Override
    public List<Job> getAllJobs(Long technicianId) {
        // Simple: all jobs where this tech is assigned
        return jobRepositoryPort.findByAssignedTechnicianAndDate(technicianId, null);
    }
}
