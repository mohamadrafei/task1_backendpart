package com.example.demo.application.port.in;

import com.example.demo.domain.Entities.MaterialUsage;

public interface AddMaterialUseCase {
    MaterialUsage addMaterial(AddMaterialCommand command);
}
