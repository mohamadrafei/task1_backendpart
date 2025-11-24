package com.example.demo.application.port.in;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.domain.Entities.DailyReport;

public interface DailyReportQueryUseCase {

    DailyReport getByCompanyAndDate(Long companyId, LocalDate date);

    List<DailyReport> getByCompanyAndRange(Long companyId, LocalDate start, LocalDate end);
}
