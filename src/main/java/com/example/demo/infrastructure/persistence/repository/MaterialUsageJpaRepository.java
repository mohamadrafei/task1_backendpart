package com.example.demo.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Entities.MaterialUsage;

@Repository
public interface MaterialUsageJpaRepository extends JpaRepository<MaterialUsage, Long> {

    List<MaterialUsage> findByJobIdIn(List<Long> jobIds);

    List<MaterialUsage> findByJobId(Long jobId);

}
