package com.example.demo.application.port.service.job;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.port.in.UpdateJobStatusCommand;
import com.example.demo.application.port.in.UpdateJobStatusUseCase;
import com.example.demo.application.port.out.JobRepositoryPort;
import com.example.demo.domain.Entities.Job;
import com.example.demo.domain.Enums.JobStatus;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateJobStatusService implements UpdateJobStatusUseCase {

    private final JobRepositoryPort jobRepository;

    @Override
    public void updateStatus(UpdateJobStatusCommand command) {
        Job job = jobRepository.findById(command.jobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // make sure this technician is the assigned one
        if (job.getAssignedTechnicianId() == null ||
                !job.getAssignedTechnicianId().equals(command.technicianId())) {
            throw new RuntimeException("You are not assigned to this job");
        }

        // (optional) validate allowed transitions
        // e.g. from ASSIGNED -> IN_PROGRESS, from IN_PROGRESS -> COMPLETED, etc.
        JobStatus newStatus = command.newStatus();
        job.setStatus(newStatus);

        jobRepository.save(job);
    }
}
