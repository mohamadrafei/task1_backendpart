package com.example.demo.application.port.in;

import com.example.demo.domain.Entities.Job;

// com.example.demo.application.port.in.CreateJobUseCase
public interface CreateJobUseCase {
    Job createJob(CreateJobCommand cmd);
}
