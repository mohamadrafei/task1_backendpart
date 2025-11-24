package com.example.demo.infrastructure.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Entities.DailyReport;

@Repository
public interface DailyJobReportJpaRepository extends JpaRepository<DailyReport, Long> {

    Optional<DailyReport> findByCompanyIdAndDate(Long companyId, LocalDate date);

    List<DailyReport> findByCompanyIdAndDateBetween(Long companyId, LocalDate start, LocalDate end);
}
