package com.example.demo.infrastructure.scheduler;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.application.port.in.GenerateDailyReportUseCase;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DailyReportScheduler {

    private final GenerateDailyReportUseCase generateDailyReportUseCase;

    // Every day at 23:55 server time
    @Scheduled(cron = "0 55 23 * * *")
    public void generateYesterdayReport() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        generateDailyReportUseCase.generateForDate(yesterday);
    }
}