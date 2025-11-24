package com.example.demo.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Entities.Company;

@Repository
public interface CompanyJpaRepository extends JpaRepository<Company, Long> {
    // You can add custom query methods here later if needed
}
