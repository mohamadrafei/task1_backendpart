// com/example/demo/infrastructure/repository/UserRepository.java
package com.example.demo.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByCompanyIdAndEmailIgnoreCase(Long companyId, String email);

    @Query("""
            select count(distinct u) from User u
            join u.roles r
            where u.companyId = :companyId
            and r = 'TECHNICIAN'
            """)
    long countTechniciansForCompany(Long companyId);

}
