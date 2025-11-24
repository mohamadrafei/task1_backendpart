package com.example.demo.application.port.in;

import com.example.demo.domain.Entities.Job;

// com.example.demo.application.port.in.GetJobUseCase
public interface GetJobUseCase {
    Job getJob(Long jobId);
}
