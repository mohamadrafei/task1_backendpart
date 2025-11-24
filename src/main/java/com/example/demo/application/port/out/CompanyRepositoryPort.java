package com.example.demo.application.port.out;

import com.example.demo.domain.Entities.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepositoryPort {

    List<Company> findAll();

    Optional<Company> findById(Long id);

    Company save(Company company);
}
