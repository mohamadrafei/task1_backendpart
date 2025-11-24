package com.example.demo.application.port.in;

import java.time.LocalDate;

public interface GenerateDailyReportUseCase {

    void generateForDate(LocalDate date);
}
