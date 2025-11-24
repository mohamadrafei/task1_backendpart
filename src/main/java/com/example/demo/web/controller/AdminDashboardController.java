package com.example.demo.web.controller;

import com.example.demo.application.port.in.AdminDashboardUseCase;
import com.example.demo.application.port.in.DailyReportUseCase;
import com.example.demo.domain.Entities.User;
import com.example.demo.web.dto.DashboardSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

        private final AdminDashboardUseCase adminDashboardUseCase;
        private final DailyReportUseCase dailyReportUseCase;

        // ----------------- DASHBOARD SUMMARY -----------------

        @PreAuthorize("hasAnyRole('ADMIN','COMPANY_ADMIN')")
        @GetMapping("/dashboard-summary")
        public DashboardSummaryResponse getDashboardSummary(
                        @AuthenticationPrincipal User currentUser) {
                if (currentUser == null) {
                        throw new ResponseStatusException(UNAUTHORIZED, "No authenticated user");
                }

                return adminDashboardUseCase.getSummary(currentUser.getCompanyId());
        }

        // ----------------- DAILY REPORT DOWNLOAD (CSV) -----------------

        @PreAuthorize("hasAnyRole('ADMIN','COMPANY_ADMIN')")
        @GetMapping("/daily-report")
        public ResponseEntity<Resource> getDailyReport(
                        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                        @AuthenticationPrincipal User currentUser) {

                if (currentUser == null) {
                        throw new ResponseStatusException(UNAUTHORIZED, "No authenticated user");
                }

                System.out.println("⚙️ Generating daily report for company="
                                + currentUser.getCompanyId() + ", date=" + date);

                byte[] csvBytes = dailyReportUseCase.generateDailyReportCsv(currentUser.getCompanyId(), date);
                String filename = "daily-report-" + date + ".csv";

                Resource resource = new ByteArrayResource(csvBytes);

                return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                                .contentType(MediaType.parseMediaType("text/csv"))
                                .contentLength(csvBytes.length)
                                .body(resource);
        }

}
