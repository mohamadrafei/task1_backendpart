package com.example.demo.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.application.port.out.CompanyRepositoryPort;
import com.example.demo.domain.Entities.Company;
import com.example.demo.infrastructure.persistence.repository.CompanyJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryAdapter implements CompanyRepositoryPort {

    private final CompanyJpaRepository jpaRepository;

    @Override
    public Optional<Company> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Company> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Company save(Company company) {
        return jpaRepository.save(company);
    }
}
