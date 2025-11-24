package com.example.demo.application.port.out;

import com.example.demo.domain.Entities.DailyReport; // 👈 add this

// application/port/out/DailyReportRepositoryPort.java
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyReportRepositoryPort {
    DailyReport save(DailyReport report);

    Optional<DailyReport> findByCompanyAndDate(Long companyId, LocalDate date);

    List<DailyReport> findByCompanyAndDateRange(Long companyId, LocalDate from, LocalDate to);
}
