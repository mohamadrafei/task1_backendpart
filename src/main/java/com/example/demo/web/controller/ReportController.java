package com.example.demo.web.controller;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.application.port.in.DailyReportQueryUseCase;
import com.example.demo.domain.Entities.DailyReport;
import com.example.demo.domain.Entities.User;
import com.example.demo.web.dto.DailyReportResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final DailyReportQueryUseCase dailyReportQueryUseCase;

    @PreAuthorize("hasAnyRole('ADMIN','COMPANY_ADMIN')")
    @GetMapping("/daily")
    public DailyReportResponse getDailyReport(
            @RequestParam(required = false) LocalDate date,
            @AuthenticationPrincipal User currentUser) {

        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
        }

        LocalDate targetDate = (date != null) ? date : LocalDate.now();

        DailyReport report = dailyReportQueryUseCase
                .getByCompanyAndDate(currentUser.getCompanyId(), targetDate);

        return DailyReportResponse.from(report);
    }

    @PreAuthorize("hasAnyRole('ADMIN','COMPANY_ADMIN')")
    @GetMapping("/daily/range")
    public List<DailyReportResponse> getDailyReportsRange(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            @AuthenticationPrincipal User currentUser) {

        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
        }

        List<DailyReport> list = dailyReportQueryUseCase
                .getByCompanyAndRange(currentUser.getCompanyId(), start, end);

        return list.stream()
                .map(DailyReportResponse::from)
                .toList();
    }
}
