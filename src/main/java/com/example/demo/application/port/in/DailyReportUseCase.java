package com.example.demo.application.port.in;

import java.time.LocalDate;

public interface DailyReportUseCase {

    /**
     * Generate a daily report for a company and date, as a CSV byte array.
     */
    byte[] generateDailyReportCsv(Long companyId, LocalDate date);
}
