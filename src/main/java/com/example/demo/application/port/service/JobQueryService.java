package com.example.demo.application.port.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.application.port.in.GetJobsForCompanyUseCase;
import com.example.demo.domain.Entities.Job;
import com.example.demo.infrastructure.persistence.repository.JobJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobQueryService implements GetJobsForCompanyUseCase {

    private final JobJpaRepository jobRepo;

    @Override
    public List<Job> getJobsForCompany(Long companyId) {
        return jobRepo.findByCompanyId(companyId);
    }
}
