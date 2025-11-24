package com.example.demo.application.port.out;

import com.example.demo.domain.Entities.MaterialUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface MaterialUsageRepository extends JpaRepository<MaterialUsage, Long> {

    @Query("""
            select coalesce(sum(m.quantity * m.unitPrice), 0)
            from MaterialUsage m
            join Job j on m.jobId = j.id
            where j.companyId = :companyId
            and m.createdAt >= :start
            and m.createdAt < :end
            """)
    double totalMaterialsCostForDay(Long companyId, LocalDateTime start, LocalDateTime end);
}
