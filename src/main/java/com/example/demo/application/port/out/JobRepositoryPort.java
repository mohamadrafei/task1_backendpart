package com.example.demo.application.port.out;

import com.example.demo.domain.Entities.Job;
import com.example.demo.domain.Enums.JobStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JobRepositoryPort {

    Job save(Job job);

    Optional<Job> findById(Long id);

    List<Job> findByCompanyAndDate(Long companyId, LocalDate date);

    List<Job> findByAssignedTechnicianAndDate(Long technicianId, LocalDate date);

    long countByCompanyId(Long companyId);

    long countByCompanyIdAndStatus(Long companyId, JobStatus status);

    long countJobsForDay(Long companyId, LocalDateTime start, LocalDateTime end);

    long countCompletedForDay(
            Long companyId,
            LocalDateTime start,
            LocalDateTime end);

    List<Job> findAllByCompanyId(Long companyId);

    List<Job> findAllByCompanyIdAndScheduledStartBetween(
            Long companyId,
            LocalDateTime start,
            LocalDateTime end);
}
