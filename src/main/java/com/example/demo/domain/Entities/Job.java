package com.example.demo.domain.Entities;

import java.time.LocalDateTime;

import com.example.demo.domain.Enums.JobStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // For now we keep a simple FK field, not a @ManyToOne
    private Long companyId;

    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private JobStatus status; // NEW / ASSIGNED / IN_PROGRESS / COMPLETED ...

    private String priority; // LOW / MEDIUM / HIGH / URGENT
    private String address;

    private Double latitude;
    private Double longitude;

    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;

    private Long assignedTechnicianId;
    private Long createdByUserId;

    // NEW: work tracking fields
    private LocalDateTime workStartedAt;
    private LocalDateTime workCompletedAt;

    // --- Getters and Setters ---

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getScheduledStart() {
        return scheduledStart;
    }

    public void setScheduledStart(LocalDateTime scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    public LocalDateTime getScheduledEnd() {
        return scheduledEnd;
    }

    public void setScheduledEnd(LocalDateTime scheduledEnd) {
        this.scheduledEnd = scheduledEnd;
    }

    public Long getAssignedTechnicianId() {
        return assignedTechnicianId;
    }

    public void setAssignedTechnicianId(Long assignedTechnicianId) {
        this.assignedTechnicianId = assignedTechnicianId;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public LocalDateTime getWorkStartedAt() {
        return workStartedAt;
    }

    public void setWorkStartedAt(LocalDateTime workStartedAt) {
        this.workStartedAt = workStartedAt;
    }

    public LocalDateTime getWorkCompletedAt() {
        return workCompletedAt;
    }

    public void setWorkCompletedAt(LocalDateTime workCompletedAt) {
        this.workCompletedAt = workCompletedAt;
    }
}
