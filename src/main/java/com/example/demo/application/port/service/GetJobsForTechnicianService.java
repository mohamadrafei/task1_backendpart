package com.example.demo.application.port.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.demo.application.port.in.GetJobsForTechnicianUseCase;
import com.example.demo.application.port.out.JobRepositoryPort;
import com.example.demo.domain.Entities.Job;
import com.example.demo.infrastructure.persistence.repository.JobJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class GetJobsForTechnicianService implements GetJobsForTechnicianUseCase {

    private final JobJpaRepository jobJpaRepository;

    @Override
    public List<Job> getJobsForDate(Long technicianId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        return jobJpaRepository.findByAssignedTechnicianIdAndScheduledStartBetween(
                technicianId,
                startOfDay,
                endOfDay);
    }

    @Override
    public List<Job> getAllJobs(Long technicianId) {
        // Simple: all jobs where this tech is assigned
        return jobJpaRepository.findByAssignedTechnicianId(technicianId);
    }
}
