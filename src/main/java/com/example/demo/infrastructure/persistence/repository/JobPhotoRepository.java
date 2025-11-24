package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.domain.Entities.JobPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPhotoRepository extends JpaRepository<JobPhoto, Long> {
}
