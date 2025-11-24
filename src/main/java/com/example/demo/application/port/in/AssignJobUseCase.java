package com.example.demo.application.port.in;

public interface AssignJobUseCase {
    void assignJob(Long jobId, Long technicianId, Long assignedByUserId);
}