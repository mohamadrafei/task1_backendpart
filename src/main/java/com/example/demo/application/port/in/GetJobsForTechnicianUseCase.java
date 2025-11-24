package com.example.demo.application.port.in;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.domain.Entities.Job;

public interface GetJobsForTechnicianUseCase {

    List<Job> getJobsForDate(Long technicianId, LocalDate date);

    List<Job> getAllJobs(Long technicianId);

}
