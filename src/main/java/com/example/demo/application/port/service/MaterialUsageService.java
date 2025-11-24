package com.example.demo.application.port.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.port.in.AddMaterialCommand;
import com.example.demo.application.port.in.AddMaterialUseCase;
import com.example.demo.application.port.in.GetMaterialsForJobUseCase;
import com.example.demo.domain.Entities.Job;
import com.example.demo.domain.Entities.MaterialUsage;
import com.example.demo.infrastructure.persistence.repository.JobJpaRepository;
import com.example.demo.infrastructure.persistence.repository.MaterialUsageJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialUsageService implements AddMaterialUseCase, GetMaterialsForJobUseCase {

    private final MaterialUsageJpaRepository materialRepo;
    private final JobJpaRepository jobRepo;

    @Override
    @Transactional
    public MaterialUsage addMaterial(AddMaterialCommand cmd) {

        Job job = jobRepo.findById(cmd.jobId())
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (job.getAssignedTechnicianId() == null ||
                !job.getAssignedTechnicianId().equals(cmd.technicianId())) {
            throw new IllegalArgumentException("Technician not assigned to this job");
        }

        if (cmd.quantity() == null || cmd.quantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }

        MaterialUsage usage = new MaterialUsage();
        usage.setJobId(cmd.jobId());
        usage.setTechnicianId(cmd.technicianId());
        usage.setName(cmd.name());
        usage.setQuantity(cmd.quantity());
        usage.setUnit(cmd.unit());
        usage.setUnitPrice(cmd.unitPrice());
        usage.setCreatedAt(LocalDateTime.now());

        return materialRepo.save(usage);
    }

    @Override
    public List<MaterialUsage> getMaterials(Long jobId) {
        return materialRepo.findByJobId(jobId); // ✅ Correct
    }
}
