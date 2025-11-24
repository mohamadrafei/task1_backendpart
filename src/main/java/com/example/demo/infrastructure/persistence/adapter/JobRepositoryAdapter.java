package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.application.port.out.JobRepositoryPort;
import com.example.demo.domain.Entities.Job;
import com.example.demo.domain.Enums.JobStatus;
import com.example.demo.infrastructure.persistence.repository.JobJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JobRepositoryAdapter implements JobRepositoryPort {

    private final JobJpaRepository jpaRepository;

    @Override
    public Job save(Job job) {
        return jpaRepository.save(job);
    }

    @Override
    public Optional<Job> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Job> findByCompanyAndDate(Long companyId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return jpaRepository.findByCompanyIdAndScheduledStartBetween(companyId, start, end);
    }

    @Override
    public List<Job> findByAssignedTechnicianAndDate(Long technicianId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return jpaRepository.findByAssignedTechnicianIdAndScheduledStartBetween(
                technicianId,
                start,
                end);
    }

    @Override
    public long countByCompanyId(Long companyId) {
        return jpaRepository.countByCompanyId(companyId);
    }

    @Override
    public long countByCompanyIdAndStatus(Long companyId, JobStatus status) {
        return jpaRepository.countByCompanyIdAndStatus(companyId, status);
    }

    @Override
    public long countJobsForDay(Long companyId, LocalDateTime start, LocalDateTime end) {
        return jpaRepository.countJobsForDay(companyId, start, end);
    }

    @Override
    public long countCompletedForDay(Long companyId, LocalDateTime start, LocalDateTime end) {
        // We hard-code COMPLETED here, so service doesn't need to pass status
        return jpaRepository.countByCompanyIdAndScheduledStartBetween(companyId, start, end);
    }

    @Override
    public List<Job> findAllByCompanyId(Long companyId) {
        return jpaRepository.findByCompanyId(companyId);
    }

    @Override
    public List<Job> findAllByCompanyIdAndScheduledStartBetween(
            Long companyId,
            LocalDateTime start,
            LocalDateTime end) {
        return jpaRepository.findByCompanyIdAndScheduledStartBetween(companyId, start, end);
    }
}
