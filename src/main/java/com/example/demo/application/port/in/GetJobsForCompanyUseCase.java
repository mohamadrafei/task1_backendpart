package com.example.demo.application.port.in;

import java.util.List;
import com.example.demo.domain.Entities.Job;

public interface GetJobsForCompanyUseCase {
    List<Job> getJobsForCompany(Long companyId);
}
