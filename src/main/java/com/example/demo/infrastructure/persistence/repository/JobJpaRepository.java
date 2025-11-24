package com.example.demo.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.Entities.Job;
import com.example.demo.domain.Enums.JobStatus;

public interface JobJpaRepository extends JpaRepository<Job, Long> {

        // BASIC COUNTS
        long countByCompanyId(Long companyId);

        long countByCompanyIdAndStatus(Long companyId, JobStatus status);

        // FOR DASHBOARD: jobs scheduled on a day
        long countByCompanyIdAndScheduledStartBetween(
                        Long companyId,
                        LocalDateTime start,
                        LocalDateTime end);

        // FOR DASHBOARD: jobs completed on a day (with status filter)
        long countByCompanyIdAndStatusAndWorkCompletedAtBetween(
                        Long companyId,
                        JobStatus status,
                        LocalDateTime start,
                        LocalDateTime end);

        // LISTING
        List<Job> findByCompanyId(Long companyId);

        List<Job> findByCompanyIdAndScheduledStartBetween(
                        Long companyId,
                        LocalDateTime start,
                        LocalDateTime end);

        // Used by daily report generator (all jobs for that day)
        List<Job> findByScheduledStartBetween(
                        LocalDateTime start,
                        LocalDateTime end);

        // If you ever filter by technician + day:
        List<Job> findByAssignedTechnicianIdAndScheduledStartBetween(
                        Long technicianId,
                        LocalDateTime start,
                        LocalDateTime end);

        List<Job> findByAssignedTechnicianId(Long technicianId);

        @Query("SELECT COUNT(j) FROM Job j WHERE j.companyId = :companyId AND j.scheduledStart >= :start AND j.scheduledStart < :end")
        long countJobsForDay(@Param("companyId") Long companyId, @Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end);

        @Query("SELECT COUNT(j) FROM Job j WHERE j.companyId = :companyId AND j.status = :status AND j.workCompletedAt >= :start AND j.workCompletedAt < :end")
        long countCompletedForDay(
                        @Param("companyId") Long companyId,
                        @Param("status") JobStatus status,
                        @Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end);

}
