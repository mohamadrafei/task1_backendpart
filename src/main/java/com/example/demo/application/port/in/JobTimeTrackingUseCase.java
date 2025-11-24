package com.example.demo.application.port.in;

public interface JobTimeTrackingUseCase {
    void startWork(Long jobId, Long technicianId);

    void completeWork(Long jobId, Long technicianId);
}
