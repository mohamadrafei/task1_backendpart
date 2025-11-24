package com.example.demo.application.port.in;

import java.util.List;

import com.example.demo.domain.Entities.MaterialUsage;

public interface GetMaterialsForJobUseCase {
    List<MaterialUsage> getMaterials(Long jobId);
}
