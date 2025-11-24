package com.example.demo.application.port.in;

import com.example.demo.web.dto.DashboardSummaryResponse;

public interface AdminDashboardUseCase {

    DashboardSummaryResponse getSummary(Long companyId);
}
