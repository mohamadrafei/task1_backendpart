package com.example.demo.domain.Entities;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "daily_job_reports", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "company_id", "date" })
})
public class DailyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(nullable = false)
    private LocalDate date;

    private Long totalJobs;
    private Long newJobs;
    private Long inProgressJobs;
    private Long completedJobs;
    private Long cancelledJobs;

    // total work time in minutes for completed jobs
    private Long totalWorkTimeMinutes;

    // sum(quantity * unitPrice) for all materials used on those jobs
    private Double totalMaterialCost;

    // --- getters & setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getTotalJobs() {
        return totalJobs;
    }

    public void setTotalJobs(Long totalJobs) {
        this.totalJobs = totalJobs;
    }

    public Long getNewJobs() {
        return newJobs;
    }

    public void setNewJobs(Long newJobs) {
        this.newJobs = newJobs;
    }

    public Long getInProgressJobs() {
        return inProgressJobs;
    }

    public void setInProgressJobs(Long inProgressJobs) {
        this.inProgressJobs = inProgressJobs;
    }

    public Long getCompletedJobs() {
        return completedJobs;
    }

    public void setCompletedJobs(Long completedJobs) {
        this.completedJobs = completedJobs;
    }

    public Long getCancelledJobs() {
        return cancelledJobs;
    }

    public void setCancelledJobs(Long cancelledJobs) {
        this.cancelledJobs = cancelledJobs;
    }

    public Long getTotalWorkTimeMinutes() {
        return totalWorkTimeMinutes;
    }

    public void setTotalWorkTimeMinutes(Long totalWorkTimeMinutes) {
        this.totalWorkTimeMinutes = totalWorkTimeMinutes;
    }

    public Double getTotalMaterialCost() {
        return totalMaterialCost;
    }

    public void setTotalMaterialCost(Double totalMaterialCost) {
        this.totalMaterialCost = totalMaterialCost;
    }
}
