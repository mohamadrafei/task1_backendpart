package com.example.demo.application.port.in;

import com.example.demo.domain.Enums.JobStatus;

public record UpdateJobStatusCommand(
        Long jobId,
        Long technicianId,
        JobStatus newStatus) {
}
